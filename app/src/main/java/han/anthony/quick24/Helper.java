package han.anthony.quick24;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by senior on 2016/10/17.
 */

public class Helper {
    private static final String TAG = "Helper";
    /**
     * 通过循环获得四个数字所有可能出现的位置
     * 第一个位置可能的数字是4种,第二个位置可能的数字是3种,第三个位置的可能的数字是两种
     * 第四个位置可能的数字是一种
     * @param originals
     * @return
     */
    public static List<int[]> getPossible4Nums(int[] originals){
         if(originals==null||originals.length!=4){
             return null;
         }

        List<int[]> possibleNums=new ArrayList<>();
        int[] newNums=new int[4];
        for(int i=0;i<4;i++){
            newNums[0]=originals[i];
            for(int j=0;j<4;j++){
                //数字不可以重复
                if(j!=i){
                    newNums[1]=originals[j];
                    for(int k=0;k<4;k++){
                        if(k!=i&&k!=j){
                            newNums[2]=originals[k];
                            for(int l=0;l<4;l++){
                                if(l!=i&&l!=j&&l!=k){
                                    newNums[3]=originals[l];
                                    int[] results=newNums.clone();
                                    possibleNums.add(results);
                                }
                            }
                        }
                    }
                }
            }
        }
        return possibleNums;
    }

    /**
     * 4个数字之间的加减乘除,最多只会出现三个运算符号
     * 获得所有可能的运算符号,共4*4*4种
     * @return
     */
    public static List<char[]> getPossibleFlags(){
        char[] flags=new char[]{'+','-','x','÷'};
        char[] newFlags=new char[3];
        List<char[]> possibleFlags=new ArrayList<>();
        for(int i=0;i<4;i++){
            newFlags[0]=flags[i];
            //运算符号可以重复
            for(int j=0;j<4;j++){
                newFlags[1]=flags[j];
                for(int k=0;k<4;k++){
                    newFlags[2]=flags[k];
                    char[] results=newFlags.clone();
                    possibleFlags.add(results);
                }
            }
        }
        for(char[] testFlags:possibleFlags){
            String a="";
            for (char flag:testFlags){
                a+=flag+" :";
            }
        }
        return possibleFlags;
    }

    /**
     * 所有可能出现的运算顺序,共五种
     * 基本运算顺序[(AB)C)D
     * 当括号中的运算符级别与括号边的运算级别不等时会有其他四种情况:
     * (AB)(CD),[A(BC)]D,A[(BC)D],A[B(CD)]
     * 当括号中的运算级别>括号边的运算级别,为无效括号,即结果中不显示括号,如A*B+C*D,运算符*级别大于+
     * 反之则为有效括号即在结果中显示括号

     * @param nums 四个可能位置的数字
     * @param flags 三个可能的运算符
     * @return  计算结果为24的String集合
     */
    public static List<String> getRightResults(int[] nums, char[] flags) {
        List<String> results=new ArrayList<>();
        String result;
        int a=nums[0];
        int b=nums[1];
        int c=nums[2];
        int d=nums[3];
        char x=flags[0];
        char y=flags[1];
        char z=flags[2];
        float cacheNum;
        float cacheNum2;
        /**
         * 基本运算顺序:[(AB)C)D
         */
        cacheNum= calculate(a,b,x);
        cacheNum= calculate(cacheNum,c,y);
        cacheNum= calculate(cacheNum,d,z);
        if(cacheNum==24){
            result="[("+a+x+b+")"+y+c+"]"+z+d;
            results.add(result);
        }
        /**
         * 运算顺序(AB)(CD)
         */
        cacheNum= calculate(a,b,x);
        cacheNum2= calculate(c,d,z);
        cacheNum= calculate(cacheNum,cacheNum2,y);
        if(cacheNum==24){
            result="("+a+x+b+")"+y+"("+c+z+d+")";
            results.add(result);
        }

        /**
         * 运算顺序[A(BC)]D
         */
        cacheNum= calculate(b,c,y);
        cacheNum= calculate(a,cacheNum,x);
        cacheNum= calculate(cacheNum,d,z);
        if(cacheNum==24){
            result="["+a+x+"("+b+y+c+")]"+z+d;
            results.add(result);
        }
        /**
         * 运算顺序A[(BC)D]
         */
        cacheNum= calculate(b,c,y);
        cacheNum= calculate(cacheNum,d,z);
        cacheNum= calculate(a,cacheNum,x);
        if(cacheNum==24){
            result=""+a+x+"[("+b+y+c+")"+z+d+"]";
            results.add(result);
        }
        /**
         * 运算顺序A[B(CD)]
         */
        cacheNum= calculate(c,d,z);
        cacheNum= calculate(b,cacheNum,y);
        cacheNum= calculate(a,cacheNum,x);
        if(cacheNum==24){
            result=""+a+x+"["+b+y+"("+c+z+d+")]";
            results.add(result);
        }

        return results;
    }

    private static float calculate(float a, float b, char flag)  {
        switch (flag){
            case '+':
                return add(a,b);
            case '-':
                return sub(a,b);
            case 'x':
                return multi(a,b);
            case '÷':
                return divide(a,b);
            default:
                Log.e(TAG, "calculate: SomeThing Wrong:"+flag );
                return 10000;
        }
    }


    public static float add(float a,float b){
        return a+b;
    }

    public static float sub(float a,float b){
        return a-b;
    }

    public static float multi(float a,float b){
        return a*b;
    }
    public static float divide(float a,float b) {
        if(b==0){
            //throw new WrongDenominatorException("分母不能为0");
           // Log.e("divide","分母不能为0");
            return (float) 1111.11111;
        }
        return a/b;
    }


}
