package han.anthony.quick24;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText mEt1, mEt2, mEt3, mEt4;
    private TextView mTextResults;
    private Button mController;
    private Button mShowAll;
    //当前显示的是计算结果
    private boolean isShowingResult = false;
    private boolean isContinue = true;
    private static final String TAG = "MainActivity";
    //保存所有位置的数字集合,如1 2 3 4, 2 4 1 3....
    private List<int[]> mPossibleNums;
    //保存所有可能的运算符号的集合,如: + - x,÷ + x.....
    private List<char[]> mPossibleFlags = Helper.getPossibleFlags();
    //保存得到24点运算过程的集合
    private List<String> mResults;
    private String num1;
    private String num2;
    private String num3;
    private String num4;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int[] abcd = (int[]) msg.obj;
            if (abcd != null && abcd.length == 4) {
                mEt1.setText(abcd[0] + "");
                mEt2.setText(abcd[1] + "");
                mEt3.setText(abcd[2] + "");
                mEt4.setText(abcd[3] + "");
                mController.setText("显示结果");
                mController.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEt1 = $(R.id.et_num1);
        mEt2 = $(R.id.et_num2);
        mEt3 = $(R.id.ev_num3);
        mEt4 = $(R.id.ev_num4);
        mTextResults = $(R.id.tv_results);
        mController = $(R.id.btn_controller);
        mShowAll = $(R.id.btn_show_all);
        createNewRandomNums();
    }

    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.btn_controller:

                if (isShowingResult) {
                    //产生新的随机数
                    createNewRandomNums();
                    isShowingResult=false;
                } else {
                    isShowingResult=true;
                    //显示计算结果
                    showOneResult();

                }
                break;
            //显示全部结果
            case R.id.btn_show_all:
                if (mResults != null) {
                    for (int i = 0; i < mResults.size(); i++) {
                        mTextResults.append(mResults.get(i) + "\n");
                    }

                }
                v.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_results:
                //点击结果时,是EditView失去焦点
                v.setSelected(true);
                return;
        }
        mTextResults.setText("");
        mShowAll.setVisibility(View.INVISIBLE);


    }

    /**
     * 随机生成1到20的数字
     */
    private void createNewRandomNums() {
        mController.setClickable(false);
        isContinue = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isContinue) {

                    int a = (int) (1 + Math.random() * 10);
                    int b = (int) (1 + Math.random() * 10);
                    int c = (int) (1 + Math.random() * 10);
                    int d = (int) (1 + Math.random() * 20);
                    int[] abcd = {a, b, c, d};
                    /**
                     * 判断产生的随机数是否可以得到24点
                     */
                    if (doseHaveResult(abcd)) {
                        isContinue = false;
                        Message msg = new Message();
                        msg.obj = abcd;
                        mHandler.sendMessage(msg);
                    }
                }
            }
        }).start();

    }

    /**
     * 显示一条满足24点的运算过程
     */
    private void showOneResult() {
        //初始化数据失败
        if(!initNums()){
            isShowingResult=false;
            return;
        }
        List<String> results = getValue24Results();
        if (results.size() == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mTextResults.setText("计算" + num1 + ":" + num2 + ":" + num3 + ":" + num4 + "没有结果!" + "  \n" + sdf.format(new Date()));
            return;
        }
        if (results.size() > 1) {
            mShowAll.setVisibility(View.VISIBLE);
            mShowAll.setText("显示全部结果:" + results.size());
        }
        mTextResults.setText(results.get(0));
        mController.setText("新的随机数");
    }

    private boolean initNums() {
        int[] nums = new int[4];
        num1 = mEt1.getText() + "";
        num2 = mEt2.getText() + "";
        num3 = mEt3.getText() + "";
        num4 = mEt4.getText() + "";
        if (TextUtils.isEmpty(num1) || TextUtils.isEmpty(num2) || TextUtils.isEmpty(num3) || TextUtils.isEmpty(num4)) {
            Toast.makeText(this, "数字不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        try{
            nums[0] = Integer.parseInt(num1);
            nums[1] = Integer.parseInt(num2);
            nums[2] = Integer.parseInt(num3);
            nums[3] = Integer.parseInt(num4);
        }catch (NumberFormatException e){
            Toast.makeText(this, "包含不合法的数字", Toast.LENGTH_SHORT).show();
        }


        mPossibleNums = Helper.getPossible4Nums(nums);

        return true;
    }

    /**
     * 检测新生成的随机数是否可以计算得到24点
     *
     * @param numbers
     * @return
     */
    private boolean doseHaveResult(int[] numbers) {
        mPossibleNums = Helper.getPossible4Nums(numbers);
        for (int[] nums : mPossibleNums) {
            for (char[] flags : mPossibleFlags) {
                if (Helper.getRightResults(nums, flags).size() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getValue24Results() {
        mResults = new ArrayList<>();
        List<String> results = new ArrayList<>();
        for (int[] nums : mPossibleNums) {
            for (char[] flags : mPossibleFlags) {

                results.addAll(Helper.getRightResults(nums, flags));
            }
        }

        /**
         * 去除重复结果
         */
        boolean isExist;
        for (String result : results) {
            isExist = false;
            for (String cacheResult : mResults) {
                if (result.equals(cacheResult)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                mResults.add(result);
                //显示结果
                //
            }
        }

        return mResults;
    }


    private <V extends View> V $(int id) {
        return (V) findViewById(id);
    }
}
