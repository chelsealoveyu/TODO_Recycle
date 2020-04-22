package com.joseph.todo_recycle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //전역변수
    SQLiteHelper dbHelper;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<Memo> memoList;
    Button btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ArrayList 선언
        dbHelper = new SQLiteHelper(MainActivity.this);
        memoList = dbHelper.selectAll();

//        //더미 데이터
//        memoList.add(new Memo("text1","testtest", 0));
//        memoList.add(new Memo("text2","testtest", 0));
//        memoList.add(new Memo("text3","testtest", 0));
//        memoList.add(new Memo("text4","testtest", 1));
//        memoList.add(new Memo("text5","testtest", 1));

        // 사용할 리사이클러뷰 지정
        recyclerView = findViewById(R.id.recyclerview);
        // 리사이클러뷰에서는 레이아웃매니저를 꼭 써야 한다.
        // 레이아웃매니저는 위젯의 형식을 지정해주는 놈이다.
        // 여러레이아웃매니저가 있고 커스텀도 가능.
        // ListView에서는 기본포맷을 제공해주는듯 함
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        // set 해주기
        recyclerView.setLayoutManager(linearLayoutManager);
        // RecyclerAdapter 지정
        recyclerAdapter = new RecyclerAdapter(memoList);
        // set 해주기
        recyclerView.setAdapter(recyclerAdapter);
        // 버튼 지정
        btnInsert = findViewById(R.id.btnInsert);

        // 저장버튼에 클릭이벤트 걸기
        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //새로운 메모 저장
                Intent intent = new Intent(MainActivity.this,InsertActivity.class);
                // startActivity 는 단순하게 어떤 액티비티를 시작하기 위해 쓴다면
                // startActivityForResult 는 시작한 액티비티를 통해 어떠한 결과값을 받기 위해 사용한다.
                startActivityForResult(intent,0);
            }
        });
    }

    // startActivityForResult로 실행한 액티비티가  끝났을때 여기에서 데이터를 받음
    // @Nullable Intent data로 아까 InsertActivity에서 보낸 데이터가 들어온다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            String strMain = data.getStringExtra("main");
            String strSub = data.getStringExtra("sub");

            Memo memo = new Memo(strMain, strSub, 0);
            recyclerAdapter.addItem(memo);
            recyclerAdapter.notifyDataSetChanged();


            dbHelper.insertMemo(memo);
        }

    }
    // 클래스 RecyclerAdapter 선언
    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{

        // 어댑터 안에서 지역변수로 쓸 listdata 선언
        //
        private List<Memo> listdata;

        // 생성자
        public RecyclerAdapter(List<Memo> listdata){
            this.listdata = listdata;
        }


        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
            Memo memo = listdata.get(i);

            itemViewHolder.maintext.setTag(memo.getSeq());
            itemViewHolder.maintext.setText(memo.getMatintext());
            itemViewHolder.subtext.setText(memo.getSubtext());
            if(memo.getIsdone()==0){
                itemViewHolder.img.setBackgroundColor(Color.LTGRAY);
            }else {
                itemViewHolder.img.setBackgroundColor(Color.GREEN);
            }
        }

        // memo에 Item 1개 추가하는 메소드
        void addItem(Memo memo){
            listdata.add(memo);
        }

        // 리스트에서 Item 1개 지우는 메소드
        void removeItem(int position){
            listdata.remove(position);
        }
        // 클래스 ItemViewHolder 선언
        class ItemViewHolder extends RecyclerView.ViewHolder{

            //지역변수
            private TextView maintext;
            private TextView subtext;
            private ImageView img;

            public ItemViewHolder(@NonNull View itemView){
                super(itemView);

                //ItemViewHolder 안에서 레이아웃에 존재하는 maintext ,subtext,img 지정해준다.
                maintext = itemView.findViewById(R.id.item_maintext);
                subtext = itemView.findViewById(R.id.item_subtext);
                img = itemView.findViewById(R.id.item_image);

                //imteView에 롱클릭이벤트 걸음
                itemView.setOnLongClickListener(new View.OnLongClickListener(){

                    @Override
                    public boolean onLongClick(View view) {

                        // 메모 하나를 롱클릭 했을때 해당 메모의 포지션 가져옴
                        int position = getAdapterPosition();
                        int seq = (int)maintext.getTag();

                        if(position != RecyclerView.NO_POSITION){
                            dbHelper.deleteMemo(seq);
                            Log.d("SQLite", "메모 삭제 완료");
                            removeItem(position);
                            notifyDataSetChanged();
                        }
                        return false;
                    }       // onLongClick
                });        // itemView.setOnLongClickListener
            }             // ItemViewHolder
        }                // class ItemViewHolder
    }                   // class RecyclerAdapter


    // 뒤로가기 버튼 누르면 앱 종료
    @Override
    public void onBackPressed(){
        // 백키 입력을 감지하면 다이얼로그 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //다이얼로그가 PositiveButton 일때 앱 종료
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }); // builder.setPositiveButton
        builder.show();

    }       // onBackPressed

}   // class MainActivity
