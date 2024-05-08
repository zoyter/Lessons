package ru.mauniver.ivtmp.lyash.quiz;

public class Question {
    private int mTextResId; // идентификатор ресурса
    private boolean mAnswerTrue; // какой ответ правильный
    // Всё что ниже сгененировано автоматически File | Settings | Editor |Code style | Java | Code Generation
    // вписать в поле Field "m" а в поле Static field "s"
    // после этого тут правой кнопкой мыши Generate ... | Getter and Setter

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public Question(int textResId, boolean answerTrue){
        mTextResId  = textResId;
        mAnswerTrue = answerTrue;
    }
}
