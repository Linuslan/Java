package com.saleoa.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

    //pinyin4j��ʽ��
//    private HanyuPinyinOutputFormat format = null;
//    //ƴ���ַ�������
//    private String[]pinyin;
//
//    //ͨ�����췽�����г�ʼ��
//    public PinyinUtil(){
//
//        format = new HanyuPinyinOutputFormat();
//        /*
//         * ������Ҫת����ƴ����ʽ
//         * ����Ϊ��
//         * HanyuPinyinToneType.WITHOUT_TONE ת��Ϊtian
//         * HanyuPinyinToneType.WITH_TONE_MARK ת��Ϊtian1
//         * HanyuPinyinVCharType.WITH_U_UNICODE ת��Ϊti��n
//         * 
//         */
//        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        pinyin = null;
//    }

    /**
     * �Ե����ֽ���ת��
     * @param pinYinStr ��ת���ĺ����ַ���
     * @return ƴ���ַ�������
     */
    private static String getCharPinYin(char pinYinStr){
    	HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        /*
         * ������Ҫת����ƴ����ʽ
         * ����Ϊ��
         * HanyuPinyinToneType.WITHOUT_TONE ת��Ϊtian
         * HanyuPinyinToneType.WITH_TONE_MARK ת��Ϊtian1
         * HanyuPinyinVCharType.WITH_U_UNICODE ת��Ϊti��n
         * 
         */
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] pinyin = null;
        try 
        {
            //ִ��ת��
            pinyin = PinyinHelper.toHanyuPinyinStringArray(pinYinStr, format);

        } catch (BadHanyuPinyinOutputFormatCombination e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //pinyin4j���򣬵�ת���ķ������Ǻ��֣��ͷ���null
        if(pinyin == null || 0 >= pinyin.length){
            return null;
        }

        //�����ֻ᷵��һ��������ƴ�������飬pinyiin4j��������Ч�жϸ��ֵĶ���
        return pinyin[0];
    }

    /**
     * �Ե����ֽ���ת��
     * @param pinYinStr
     * @return
     */
    public static String getStringPinYin(String pinYinStr){
        StringBuffer sb = new StringBuffer();
        String tempStr = null;
        //ѭ���ַ���
        for(int i = 0; i<pinYinStr.length(); i++)
        {

            tempStr = getCharPinYin(pinYinStr.charAt(i));
            if(tempStr == null)
            {
                //�Ǻ���ֱ��ƴ��
                sb.append(pinYinStr.charAt(i));
            }
            else
            {
                sb.append(tempStr);
            }
        }

        return sb.toString();

    }
}