package org.komine.wca.api.rest.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.komine.wca.api.rest.util.ArgsUtil;

public class ArgsUtilTest {

	@Test
	public void testGet01() {
		String[] args = {"-aaa", "111", "-bbb", "222", "-ccc", "-ddd", "444", "-eee"};
		ArgsUtil argsUtil = new ArgsUtil(args);
		
		Assert.assertEquals("111", argsUtil.get("aaa"));
		Assert.assertEquals("222", argsUtil.get("bbb"));
		Assert.assertNull(argsUtil.get("ccc"));
		Assert.assertEquals("444", argsUtil.get("ddd"));
		Assert.assertNull(argsUtil.get("eee"));
		
		Assert.assertNull(argsUtil.get("xxx"));
	}
	
	@Test
	public void testGet02() {
		String[] args = {"-Aaa", "111", "-bBb", "222", "-ccC", "-DDd", "444", "-eEE"};
		ArgsUtil argsUtil = new ArgsUtil(args);
		
		Assert.assertEquals("111", argsUtil.get("aaA"));
		Assert.assertEquals("222", argsUtil.get("bBb"));
		Assert.assertNull(argsUtil.get("Ccc"));
		Assert.assertEquals("444", argsUtil.get("ddD"));
		Assert.assertNull(argsUtil.get("Eee"));
		
		Assert.assertNull(argsUtil.get("xxx"));
	}
	
	@Test
	public void testIsExist01() {
		String[] args = {"-aaa", "111", "-bbb", "222", "-ccc", "-ddd", "444", "-eee"};
		ArgsUtil argsUtil = new ArgsUtil(args);
		
		Assert.assertTrue(argsUtil.isExist("aaa"));
		Assert.assertTrue(argsUtil.isExist("bbb"));
		Assert.assertTrue(argsUtil.isExist("ccc"));
		Assert.assertTrue(argsUtil.isExist("ddd"));
		Assert.assertTrue(argsUtil.isExist("eee"));
		
		Assert.assertFalse(argsUtil.isExist("xxx"));
	}

	@Test
	public void testIsExist02() {
		String[] args = {"-Aaa", "111", "-bBb", "222", "-ccC", "-DDd", "444", "-eEE"};
		ArgsUtil argsUtil = new ArgsUtil(args);
		
		Assert.assertTrue(argsUtil.isExist("AAA"));
		Assert.assertTrue(argsUtil.isExist("BbB"));
		Assert.assertTrue(argsUtil.isExist("CCc"));
		Assert.assertTrue(argsUtil.isExist("dDD"));
		Assert.assertTrue(argsUtil.isExist("Eee"));
		
		Assert.assertFalse(argsUtil.isExist("xxx"));
	}
}
