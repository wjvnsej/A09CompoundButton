package com.kosmo.a09compoundbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/*
각 위젯을 선택(체크) 했을 때를 감지하기 위해
    1. CompoundButton.OnCheckedChangeListener 를 implements 하고
    2. onCheckedChanged() 메소드를 오버라이딩 처리한다.(코드 아래쪽에 있음)
 */
public class MainActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

    //체크박스에서 선택한 값을 저장하기 위한 용도의 컬렉션
    private List checkBoxList = new Vector();

    /*
    스피너에게 어뎁터 객체를 통해 데이터(목록)로 사용될 문자열 배열
     */
    private  String[] items = {"레드벨벳","트와이스","마마무","블랙핑크","에이핑크","오마이걸","피에스타"};

    //결과출력용 전역변수(라디오, 토글버튼, 스위치, 스피너)
    private String radioChecked = "여성";
    private String spinnerSelected = "트와이스";
    private String toggleOnOff = "OFF";
    private String switchOnOff = "OFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //결과를 출력하는 텍스트뷰에 대한 설정
        final TextView tv = (TextView)findViewById(R.id.textview);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); //텍스트크기 설정
        tv.setTypeface(Typeface.SANS_SERIF); ////폰트 타입 설정

        //체크박스 위젯 가져오기
        CheckBox chk_eco = (CheckBox)findViewById(R.id.check_eco);
        CheckBox chk_pol = (CheckBox)findViewById(R.id.check_pol);
        CheckBox chk_spo = (CheckBox)findViewById(R.id.check_spo);
        CheckBox chk_ent = (CheckBox)findViewById(R.id.check_ent);

        //Java에서 체크된 상태로 설정함(XML에서는 checked속성을 사용함)
        chk_eco.setChecked(true); //경제 항목체크
        chk_pol.setChecked(true); //정치 항목체크
        //미리 선택한 항목을 List컬렉션에 저장함
        checkBoxList.add("정치");
        checkBoxList.add("경제");
        checkBoxList.add("연애"); //xml에서 선택했음

        //각 체크박스에 리스너부착. 해당 리스너는 onCheckedChanged() 메소드를 호출하게 된다.
        chk_eco.setOnCheckedChangeListener(this);
        chk_ent.setOnCheckedChangeListener(this);
        chk_spo.setOnCheckedChangeListener(this);
        chk_pol.setOnCheckedChangeListener(this);

        /*
        라디오의 경우 반드시 라디오 그룹을 통해 접근한다.
         */
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.clearCheck(); //라디오버튼 체크해제
        radioGroup.check(R.id.radio_female); //여성을 디폴트로 체크

        //라디오그룹에 리스너 부착 후 익명클래스를 통해 오버라이딩 처리
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /*
            라디오그룹을 통해 선택된 라디오버튼의 id값이 매개변수로 전달된다.
            int형 매개변수 i를 통해 리소스아이디를 얻어올 수 있다.
             */
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radio = (RadioButton)findViewById(i);
                radioChecked = radio.getText().toString();
                Toast.makeText(MainActivity.this,
                        radioChecked, Toast.LENGTH_SHORT).show();
            }
        });

        //토글버튼, 스위치 위젯을 얻어와서 객체에 저장
        ToggleButton toggleButton = (ToggleButton)findViewById(R.id.togglebutton);
        toggleButton.setChecked(false); //off 상태로 변경

        Switch switch_btn = (Switch)findViewById(R.id.switchbutton);
        switch_btn.setChecked(false); //off 상태로 변경
        //각각 리스너 부착
        toggleButton.setOnCheckedChangeListener(this);
        switch_btn.setOnCheckedChangeListener(this);

        //스피너 위젯 가져와서 객체와
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        /*
        어댑터 객체 생성
            사용법 : new ArrayAdapter<제네릭>(컨텍스트, 스피너모양, 항목);
            ※ 현재 안드로이드에서 기본으로 제공하는 레이아웃을 사용하고 있음
            ※ 항목은 전역변수로 선언한 문자열 배열을 사용
         */
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter); //스피너에 어탭터 적용
        /*
        앱이 실행될 때 디폴트로 선택된 상태를 반들어주려면 반드시 jsva에서 처리해야 한다.
        xml에는 select라는 속성이 별도로 없기 때문이다.
        아래에서 setSelected()을 실행하면, onItemSelected() 콜백메소드가 호출된다.
         */
        spinner.setSelection(1); //1번 인덱스를 디폴트로 선택함(트와이스)
        /*
        스피너는 아래와 같이 setOnItemSelectedListener 리스너를 부착해야 함.
        항목을 선택할 때 View와 index 등을 통해 콜백메소드로 전달한다.
         */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
            매개변수
                adapterView : 여기서는 spinner를 의미한다.
                i : 어댑터의 위치값, 즉 사용자가 선택한 항목의 인덱스가 전달됨
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,
                        items[i],
                        Toast.LENGTH_SHORT).show();
                spinnerSelected = items[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //버튼가져오기
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tv.setText(String.format("체크박스 : %s\n라디오 : %s\n토글 : %s\n스위치 : %s\n스피너 : %s\n",
                        Arrays.toString(checkBoxList.toArray()),
                        radioChecked,
                        toggleOnOff,
                        switchOnOff,
                        spinnerSelected));
            }
        });

    }////onCreate End

    //위젯을 선택(체크)했을 때 해당 이벤트를 감지하는 메소드(오버라이딩)
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //컴파운드버튼이 체크박스라면..
        if(compoundButton instanceof CheckBox){
            if(b == true){
                //체크된 상태일 때..
                Toast.makeText(this,
                        compoundButton.getText() + "선택함",
                        Toast.LENGTH_SHORT).show();
                //List컬렉션에 추가함
                checkBoxList.add(compoundButton.getText());
            }
            else{
                //체크를 해제했다면
                Toast.makeText(this,
                        compoundButton.getText() + "해제함",
                        Toast.LENGTH_SHORT).show();
                //List컬렉션에서 제거함
                checkBoxList.remove(compoundButton.getText());
            }
        }
        else if(compoundButton.getId() == R.id.togglebutton){
            //토글버튼일때..
            if(b == true){
                Toast.makeText(this, "ON상태", Toast.LENGTH_SHORT).show();
                toggleOnOff = "ON";
            }
            else{
                Toast.makeText(this, "OFF상태", Toast.LENGTH_SHORT).show();
                toggleOnOff = "OFF";
            }
        }
        else if(compoundButton.getId() == R.id.switchbutton){
            //스위치일때..
            if(b == true){
                Toast.makeText(this, "스위치ON", Toast.LENGTH_SHORT).show();
                switchOnOff = "스위치ON";
            }
            else{
                Toast.makeText(this, "스위치OFF", Toast.LENGTH_SHORT).show();
                switchOnOff = "스위치OFF";
            }
        }
    }////onCheckedChanged End

}



















