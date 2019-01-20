package test.com.yy.utils;

import com.yy.entity.TranslateType;
import com.yy.utils.ExcelUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ExcelUtil Tester.
 *
 * @author zhangcanlogn
 * @version 1.0
 * @since 2019-1-20
 */
public class ExcelUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getTestStr()
     */
    @Test
    public void testGetTestStr() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getBaiduLanguageAndCodeFromExcel(String str)
     */
    @Test
    public void getTranslateLanguageAndCodeFromExcel() throws Exception {

        System.out.println(ExcelUtil.getTranslateLanguageAndCodeFromExcel(TranslateType.BAIDU));

        System.out.println(ExcelUtil.getTranslateLanguageAndCodeFromExcel(TranslateType.GOOGLE));

//TODO: Test goes here... 
    }

    /**
     * Method: getLanguageAndUnicodeFromExcel()
     */
    @Test
    public void testGetLanguageAndUnicodeFromExcel() throws Exception {
//TODO: Test goes here... 
    }


    /**
     * Method: getExcelInfo()
     */
    @Test
    public void testGetExcelInfo() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = ExcelUtil.getClass().getMethod("getExcelInfo"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
