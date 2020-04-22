package com.joseph.todo_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertActivity extends AppCompatActivity {

    //전역변수
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        editText = findViewById(R.id.edtMemo);

        // 입력화면에서 입력 버튼 누를시
        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // str 변수에 텍스트창에 입력된 데이터 넣기
                String str = editText.getText().toString();

                if(str.length() >0){
                    str.trim();
                    // date 객체 선언
                    Date date = new Date();
                    // 날짜 포맷 가져와서 담기
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String substr = sdf.format(date);

                    // MainActivity로 보낼 인텐트 선언
                    Intent intent = new Intent();
                    // 키값 지정해서 putExtra
                    intent.putExtra("main",str);
                    intent.putExtra("sub",substr);
                    setResult(RESULT_OK,intent);
                    //액티비티 종료
                    finish();

                    Toast.makeText(InsertActivity.this,str+","+ substr,Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 입력화면에서 취소 버튼 누를시
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //취소 버튼 누르면 액티비티 종료
                finish();
            }
        });
    }
}
