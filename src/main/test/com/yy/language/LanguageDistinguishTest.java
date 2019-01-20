package com.yy.language;

import com.yy.utils.ExcelUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * LanguageDistinguish Tester.
 *
 * @author zhagncanlong
 * @version 1.0
 * @since 2019-1-20
 */
public class LanguageDistinguishTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getLanguageByString(String str)
     */
    @Test
    public void testGetLanguageByString() throws Exception {
        LanguageDistinguish test = new LanguageDistinguish();
        List<String> list = ExcelUtil.getTestStr();
        System.out.println("开始时间：" + new Date());
        long count = 0;
        for (int i = 0; i < 1000_000; i++) {
            System.out.println(i + "次，" + test.getLanguageByString(list.get(i % list.size())));
            //添加1秒停顿以上使用谷歌翻译
            try {
                Thread.sleep(960);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("结束时间：" + new Date() + "\n次数：" + count);

    }

    /**
     * Method: testRange()
     */
    @Test
    public void testTestRange() throws Exception {
//TODO: Test goes here... 
    }


    /**
     * Method: getLanguageFromGoogle(String str)
     */
    @Test
    public void testGetLanguageFromGoogle() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = LanguageDistinguish.getClass().getMethod("getLanguageFromGoogle", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getLanguageFromBaidu(String str)
     */
    @Test
    public void testGetLanguageFromBaidu() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = LanguageDistinguish.getClass().getMethod("getLanguageFromBaidu", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getOneLanguageByUnicode(int unicode)
     */
    @Test
    public void testGetOneLanguageByUnicode() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = LanguageDistinguish.getClass().getMethod("getOneLanguageByUnicode", int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: findIndexFromUnicodes(int[] unicodesRange, int unicode)
     */
    @Test
    public void testFindIndexFromUnicodes() throws Exception {
        try {
            LanguageDistinguish test = new LanguageDistinguish();
            Method method = LanguageDistinguish.class.getDeclaredMethod("findIndexFromUnicodes", int[].class, int.class);
            method.setAccessible(true);

            //功能测试1，多个元素，刚好等于某个值
            int[] range1 = {1, 3, 5, 7, 10, 12};
            // System.out.println(this.findIndexFromUnicodes(range1, 12));
            System.out.println(method.invoke(test, range1, 12));
            //功能测试2，多个元素，在某个区间内
            System.out.println(method.invoke(test, range1, 11));
            //功能测试3，多个元素，不在在某个区间内
            System.out.println(method.invoke(test, range1, 9));
            //功能测试4，2个元素，在或不在某个区间内
            int[] range2 = {1, 3};
            System.out.println(method.invoke(test, range1, 2));
            //特殊测试1，空
            System.out.println(method.invoke(test, null, 2));
            //一个元素
            int[] range3 = {1};
            System.out.println(method.invoke(test, range3, 2));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method: token(String text)
     */
    @Test
    public void testToken() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = LanguageDistinguish.getClass().getMethod("token", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
