/**
 * 
 */
package org.sagacity.sqltoy.utils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.SqlToyContext;
import org.sagacity.sqltoy.demo.domain.StaffInfo;
import org.sagacity.sqltoy.demo.vo.LoginLogVO;
import org.sagacity.sqltoy.demo.vo.StaffInfoVO;
import org.sagacity.sqltoy.demo.vo.SysLoginLog;

import com.alibaba.fastjson.JSON;

/**
 * @project sagacity-sqltoy
 * @description 请在此说明类的功能
 * @author zhong
 * @version v1.0, Date:2020-8-10
 * @modify 2020-8-10,修改说明
 */
public class MapperUtilsTest {
	@Test
	public void testVOToPO() {
		StaffInfoVO staffInfoVO = new StaffInfoVO();
		staffInfoVO.setStaffId("S2007");
		staffInfoVO.setStaffCode("S2007");
		staffInfoVO.setPostType("MASTER");
		staffInfoVO.setStaffName("测试员工9");
		staffInfoVO.setSexType("M");
		staffInfoVO.setEmail("test3@aliyun.com");
		staffInfoVO.setEntryDate(LocalDate.now());
		staffInfoVO.setStatus(1);
		staffInfoVO.setOrganId("100007");
		// staffInfoVO.setPhoto(FileUtil.readAsBytes("classpath:/mock/staff_photo.jpg"));
		staffInfoVO.setCountry("86");
		SqlToyContext context = new SqlToyContext();
		try {
			context.initialize();
			StaffInfo staffInfo = MapperUtils.map(staffInfoVO, StaffInfo.class, "sexType");
			System.err.println(JSON.toJSONString(staffInfo));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testPOTOVO() {
		StaffInfo staffInfo = new StaffInfo();
		staffInfo.setStaffId("S2007");
		staffInfo.setStaffCode("S2007");
		staffInfo.setPost("MASTER");
		staffInfo.setStaffName("测试员工9");
		staffInfo.setSexType("M");
		staffInfo.setEmail("test3@aliyun.com");
		staffInfo.setEntryDate(LocalDate.now());
		staffInfo.setStatus(1);
		staffInfo.setOrganId("100007");
		// staffInfoVO.setPhoto(FileUtil.readAsBytes("classpath:/mock/staff_photo.jpg"));
		staffInfo.setCountry("86");
		SqlToyContext context = new SqlToyContext();
		try {
			context.initialize();
			StaffInfoVO staffInfoVO = MapperUtils.map(staffInfo, StaffInfoVO.class);
			System.err.println(JSON.toJSONString(staffInfoVO));
			StaffInfo staffInfo1 = MapperUtils.map(staffInfoVO, StaffInfo.class);
			System.err.println(JSON.toJSONString(staffInfo1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testPOTOVO1() {
		LoginLogVO loginLogVO = new LoginLogVO();
		loginLogVO.setLogId(new BigInteger("1"));
		loginLogVO.setLoginTime(LocalDateTime.now());
		SysLoginLog sysLoginLog = MapperUtils.map(loginLogVO, SysLoginLog.class);
		System.err.println(JSON.toJSONString(sysLoginLog));

	}
}
