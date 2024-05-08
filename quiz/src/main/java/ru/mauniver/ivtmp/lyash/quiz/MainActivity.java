package ru.mauniver.ivtmp.lyash.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private TextView mQuestionTextView;

    //        Читы
    private Button mCheatButton;
    private static final int REQUEST_CODE_CHEAT = 7877;
    private boolean mIsCheater;



    // Для сохранения данных между поворотами
    private static final String tag = "Quiz";
    private static final String KEY_INDEX = "index";

    private  Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_asia,true)
    };
    private int mCurrentIndex = 0;

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

//    Читы
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        }else{
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Сохранение данных между поворотами
        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
//        int question = mQuestionBank[mCurrentIndex].getTextResId();
//        mQuestionTextView.setText(question);

        mTrueButton = (Button) findViewById(R.id.true_button);

        mFalseButton = (Button) findViewById(R.id.false_button);

        mTrueButton.setOnClickListener(view ->{
//            Toast.makeText(MainActivity.this,R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
            checkAnswer(true);
        });

        mFalseButton.setOnClickListener(view ->{
//                Toast.makeText(MainActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
            checkAnswer(false);
        });

        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(view->{
            mCurrentIndex = (mCurrentIndex+1)%mQuestionBank.length;
            mIsCheater = false;
//            int question = mQuestionBank[mCurrentIndex].getTextResId();
//            mQuestionTextView.setText(question);
            updateQuestion();
        });
        updateQuestion();

        mQuestionTextView.setOnClickListener(view ->{
            updateQuestion();
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(view ->{
            mCurrentIndex = (mCurrentIndex-1);
            if (mCurrentIndex < 0) {
                mCurrentIndex=mQuestionBank.length-1;
            };
            updateQuestion();
        });


//        Читы
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск CheatActivity
//                Intent i = new Intent(MainActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].
                        isAnswerTrue();
                Intent i = CheatActivity.newIntent(MainActivity.this,
                        answerIsTrue);
//                startActivity(i);
                startActivityForResult(i, REQUEST_CODE_CHEAT);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("My tag","onActivityResult");
        if (resultCode != MainActivity.RESULT_OK){
            Log.d("My tag","RESULT_OK");
            return;
        }
        Log.d("My tag","RESULT_CANCEL");
        if (resultCode == REQUEST_CODE_CHEAT){
            if (data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
        mIsCheater = CheatActivity.wasAnswerShown(data);
        Log.d("My tag",String.valueOf(mIsCheater));
    }

    // Сохранение данных между поворотами
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
}
