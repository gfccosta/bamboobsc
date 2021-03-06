/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package com.netsteadfast.greenstep.bsc.service.logic.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.netsteadfast.greenstep.base.Constants;
import com.netsteadfast.greenstep.base.SysMessageUtil;
import com.netsteadfast.greenstep.base.exception.ServiceException;
import com.netsteadfast.greenstep.base.model.DefaultResult;
import com.netsteadfast.greenstep.base.model.GreenStepSysMsgConstants;
import com.netsteadfast.greenstep.base.model.ServiceAuthority;
import com.netsteadfast.greenstep.base.model.ServiceMethodAuthority;
import com.netsteadfast.greenstep.base.model.ServiceMethodType;
import com.netsteadfast.greenstep.base.model.YesNo;
import com.netsteadfast.greenstep.base.service.logic.BscBaseLogicService;
import com.netsteadfast.greenstep.bsc.service.IDegreeFeedbackAssignService;
import com.netsteadfast.greenstep.bsc.service.IEmployeeOrgaService;
import com.netsteadfast.greenstep.bsc.service.IKpiEmplService;
import com.netsteadfast.greenstep.bsc.service.IMeasureDataService;
import com.netsteadfast.greenstep.bsc.service.IMonitorItemScoreService;
import com.netsteadfast.greenstep.bsc.service.IPdcaItemOwnerService;
import com.netsteadfast.greenstep.bsc.service.IPdcaOwnerService;
import com.netsteadfast.greenstep.bsc.service.IReportRoleViewService;
import com.netsteadfast.greenstep.bsc.service.logic.IEmployeeLogicService;
import com.netsteadfast.greenstep.po.hbm.BbDegreeFeedbackAssign;
import com.netsteadfast.greenstep.po.hbm.BbEmployeeOrga;
import com.netsteadfast.greenstep.po.hbm.BbKpiEmpl;
import com.netsteadfast.greenstep.po.hbm.BbMeasureData;
import com.netsteadfast.greenstep.po.hbm.BbMonitorItemScore;
import com.netsteadfast.greenstep.po.hbm.BbPdcaItemOwner;
import com.netsteadfast.greenstep.po.hbm.BbPdcaOwner;
import com.netsteadfast.greenstep.po.hbm.BbReportRoleView;
import com.netsteadfast.greenstep.po.hbm.TbSysCalendarNote;
import com.netsteadfast.greenstep.po.hbm.TbSysMsgNotice;
import com.netsteadfast.greenstep.po.hbm.TbUserRole;
import com.netsteadfast.greenstep.service.ISysCalendarNoteService;
import com.netsteadfast.greenstep.service.ISysMsgNoticeService;
import com.netsteadfast.greenstep.service.logic.IRoleLogicService;
import com.netsteadfast.greenstep.vo.AccountVO;
import com.netsteadfast.greenstep.vo.DegreeFeedbackAssignVO;
import com.netsteadfast.greenstep.vo.EmployeeOrgaVO;
import com.netsteadfast.greenstep.vo.EmployeeVO;
import com.netsteadfast.greenstep.vo.KpiEmplVO;
import com.netsteadfast.greenstep.vo.MeasureDataVO;
import com.netsteadfast.greenstep.vo.MonitorItemScoreVO;
import com.netsteadfast.greenstep.vo.OrganizationVO;
import com.netsteadfast.greenstep.vo.PdcaItemOwnerVO;
import com.netsteadfast.greenstep.vo.PdcaOwnerVO;
import com.netsteadfast.greenstep.vo.ReportRoleViewVO;
import com.netsteadfast.greenstep.vo.SysCalendarNoteVO;
import com.netsteadfast.greenstep.vo.SysMsgNoticeVO;
import com.netsteadfast.greenstep.vo.UserRoleVO;

@ServiceAuthority(check=true)
@Service("bsc.service.logic.EmployeeLogicService")
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class EmployeeLogicServiceImpl extends BscBaseLogicService implements IEmployeeLogicService {
	protected Logger logger=Logger.getLogger(EmployeeLogicServiceImpl.class);
	private IEmployeeOrgaService<EmployeeOrgaVO, BbEmployeeOrga, String> employeeOrgaService;
	private ISysMsgNoticeService<SysMsgNoticeVO, TbSysMsgNotice, String> sysMsgNoticeService;
	private ISysCalendarNoteService<SysCalendarNoteVO, TbSysCalendarNote, String> sysCalendarNoteService;
	private IReportRoleViewService<ReportRoleViewVO, BbReportRoleView, String> reportRoleViewService;
	private IKpiEmplService<KpiEmplVO, BbKpiEmpl, String> kpiEmplService;
	private IDegreeFeedbackAssignService<DegreeFeedbackAssignVO, BbDegreeFeedbackAssign, String> degreeFeedbackAssignService;
	private IMeasureDataService<MeasureDataVO, BbMeasureData, String> measureDataService;
	private IPdcaOwnerService<PdcaOwnerVO, BbPdcaOwner, String> pdcaOwnerService;
	private IPdcaItemOwnerService<PdcaItemOwnerVO, BbPdcaItemOwner, String> pdcaItemOwnerService;
	private IRoleLogicService roleLogicService;
	private IMonitorItemScoreService<MonitorItemScoreVO, BbMonitorItemScore, String> monitorItemScoreService;
	
	public EmployeeLogicServiceImpl() {
		super();
	}
	
	public IEmployeeOrgaService<EmployeeOrgaVO, BbEmployeeOrga, String> getEmployeeOrgaService() {
		return employeeOrgaService;
	}

	@Autowired
	@Resource(name="bsc.service.EmployeeOrgaService")
	@Required	
	public void setEmployeeOrgaService(IEmployeeOrgaService<EmployeeOrgaVO, BbEmployeeOrga, String> employeeOrgaService) {
		this.employeeOrgaService = employeeOrgaService;
	}	
	
	public ISysMsgNoticeService<SysMsgNoticeVO, TbSysMsgNotice, String> getSysMsgNoticeService() {
		return sysMsgNoticeService;
	}

	@Autowired
	@Resource(name="core.service.SysMsgNoticeService")
	@Required			
	public void setSysMsgNoticeService(
			ISysMsgNoticeService<SysMsgNoticeVO, TbSysMsgNotice, String> sysMsgNoticeService) {
		this.sysMsgNoticeService = sysMsgNoticeService;
	}

	public ISysCalendarNoteService<SysCalendarNoteVO, TbSysCalendarNote, String> getSysCalendarNoteService() {
		return sysCalendarNoteService;
	}

	@Autowired
	@Resource(name="core.service.SysCalendarNoteService")
	@Required	
	public void setSysCalendarNoteService(
			ISysCalendarNoteService<SysCalendarNoteVO, TbSysCalendarNote, String> sysCalendarNoteService) {
		this.sysCalendarNoteService = sysCalendarNoteService;
	}
	
	public IReportRoleViewService<ReportRoleViewVO, BbReportRoleView, String> getReportRoleViewService() {
		return reportRoleViewService;
	}

	@Autowired
	@Resource(name="bsc.service.ReportRoleViewService")
	@Required		
	public void setReportRoleViewService(
			IReportRoleViewService<ReportRoleViewVO, BbReportRoleView, String> reportRoleViewService) {
		this.reportRoleViewService = reportRoleViewService;
	}

	public IKpiEmplService<KpiEmplVO, BbKpiEmpl, String> getKpiEmplService() {
		return kpiEmplService;
	}

	@Autowired
	@Resource(name="bsc.service.KpiEmplService")
	@Required		
	public void setKpiEmplService(
			IKpiEmplService<KpiEmplVO, BbKpiEmpl, String> kpiEmplService) {
		this.kpiEmplService = kpiEmplService;
	}

	public IDegreeFeedbackAssignService<DegreeFeedbackAssignVO, BbDegreeFeedbackAssign, String> getDegreeFeedbackAssignService() {
		return degreeFeedbackAssignService;
	}

	@Autowired
	@Resource(name="bsc.service.DegreeFeedbackAssignService")
	@Required		
	public void setDegreeFeedbackAssignService(
			IDegreeFeedbackAssignService<DegreeFeedbackAssignVO, BbDegreeFeedbackAssign, String> degreeFeedbackAssignService) {
		this.degreeFeedbackAssignService = degreeFeedbackAssignService;
	}

	public IMeasureDataService<MeasureDataVO, BbMeasureData, String> getMeasureDataService() {
		return measureDataService;
	}

	@Autowired
	@Resource(name="bsc.service.MeasureDataService")
	@Required		
	public void setMeasureDataService(
			IMeasureDataService<MeasureDataVO, BbMeasureData, String> measureDataService) {
		this.measureDataService = measureDataService;
	}
	
	public IPdcaOwnerService<PdcaOwnerVO, BbPdcaOwner, String> getPdcaOwnerService() {
		return pdcaOwnerService;
	}

	@Autowired
	@Resource(name="bsc.service.PdcaOwnerService")
	@Required		
	public void setPdcaOwnerService(IPdcaOwnerService<PdcaOwnerVO, BbPdcaOwner, String> pdcaOwnerService) {
		this.pdcaOwnerService = pdcaOwnerService;
	}

	public IPdcaItemOwnerService<PdcaItemOwnerVO, BbPdcaItemOwner, String> getPdcaItemOwnerService() {
		return pdcaItemOwnerService;
	}

	@Autowired
	@Resource(name="bsc.service.PdcaItemOwnerService")
	@Required		
	public void setPdcaItemOwnerService(
			IPdcaItemOwnerService<PdcaItemOwnerVO, BbPdcaItemOwner, String> pdcaItemOwnerService) {
		this.pdcaItemOwnerService = pdcaItemOwnerService;
	}
	
	public IRoleLogicService getRoleLogicService() {
		return roleLogicService;
	}

	@Autowired
	@Resource(name="core.service.logic.RoleLogicService")
	@Required		
	public void setRoleLogicService(IRoleLogicService roleLogicService) {
		this.roleLogicService = roleLogicService;
	}	
	
	public IMonitorItemScoreService<MonitorItemScoreVO, BbMonitorItemScore, String> getMonitorItemScoreService() {
		return monitorItemScoreService;
	}

	@Autowired
	@Resource(name="bsc.service.MonitorItemScoreService")
	@Required	
	public void setMonitorItemScoreService(
			IMonitorItemScoreService<MonitorItemScoreVO, BbMonitorItemScore, String> monitorItemScoreService) {
		this.monitorItemScoreService = monitorItemScoreService;
	}	
	
	private boolean isAdministrator(String account) {
		if (account.equals("admin") || account.equals(Constants.SYSTEM_BACKGROUND_USER)) {
			return true;
		}
		return false;
	}	
	
	private AccountVO tranAccount(EmployeeVO employee) throws Exception {
		AccountVO account = new AccountVO();
		account.setAccount(employee.getAccount());
		account.setOnJob(YesNo.YES);
		account.setPassword( this.getAccountService().tranPassword(employee.getPassword()) );		
		return account;
	}

	@ServiceMethodAuthority(type={ServiceMethodType.INSERT})
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<EmployeeVO> create(EmployeeVO employee, List<String> organizationOid) throws ServiceException, Exception {
		if (employee==null || super.isBlank(employee.getEmpId())) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.PARAMS_BLANK));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("empId", employee.getEmpId());
		if (this.getEmployeeService().countByParams(params) > 0) {
			throw new ServiceException("Please change another Id!");
		}
		AccountVO account = this.tranAccount(employee);
		if (this.isAdministrator(account.getAccount())) {
			throw new ServiceException("Please change another Account!");
		}		
		
		DefaultResult<AccountVO> mResult = this.getAccountService().saveObject(account);
		if (mResult.getValue()==null) {
			throw new ServiceException( mResult.getSystemMessage().getValue() );
		}		
		DefaultResult<EmployeeVO> result = this.getEmployeeService().saveObject(employee);
		this.createEmployeeOrganization(result.getValue(), organizationOid);
		
		// create default role
		UserRoleVO userRole = new UserRoleVO();
		userRole.setAccount(result.getValue().getAccount());
		userRole.setRole( this.roleLogicService.getDefaultUserRole() );
		userRole.setDescription(result.getValue().getAccount() + " `s role!");
		this.getUserRoleService().saveObject(userRole);
		
		return result;
	}

	@ServiceMethodAuthority(type={ServiceMethodType.UPDATE})
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<EmployeeVO> update(EmployeeVO employee, List<String> organizationOid) throws ServiceException, Exception {
		if (employee==null || super.isBlank(employee.getOid()) ) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.PARAMS_BLANK));
		}
		EmployeeVO dbEmployee = this.findEmployeeData( employee.getOid() );
		this.deleteEmployeeOrganization( dbEmployee );
		employee.setAccount(dbEmployee.getAccount() );
		employee.setEmpId( dbEmployee.getEmpId() );	
		DefaultResult<EmployeeVO> result = this.getEmployeeService().updateObject(employee);
		if (result.getValue()==null) {
			throw new ServiceException(result.getSystemMessage().getValue());
		}
		this.createEmployeeOrganization(result.getValue(), organizationOid);		
		return result;
	}

	@ServiceMethodAuthority(type={ServiceMethodType.UPDATE})
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<AccountVO> updatePassword(EmployeeVO employee, String newPassword) throws ServiceException, Exception {
		if (employee==null || super.isBlank(employee.getOid()) 
				|| super.isBlank(employee.getPassword()) || super.isBlank(newPassword) ) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.PARAMS_BLANK));
		}
		EmployeeVO dbEmployee = this.findEmployeeData(employee.getOid());
		AccountVO account = this.findAccountData( dbEmployee.getAccount() );
		if (!account.getPassword().equals( this.getAccountService().tranPassword(employee.getPassword()) ) ) {
			throw new ServiceException("The current password(old password) is incorrect!");
		}		
		account.setPassword( this.getAccountService().tranPassword(newPassword) );		
		return getAccountService().updateObject(account);
	}

	@ServiceMethodAuthority(type={ServiceMethodType.DELETE})
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> delete(EmployeeVO employee) throws ServiceException, Exception {
		if (employee==null || super.isBlank(employee.getOid()) ) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.PARAMS_BLANK));
		}
		employee = this.findEmployeeData(employee.getOid());
		AccountVO account = this.findAccountData(employee.getAccount());
		if (this.isAdministrator(account.getAccount())) {
			throw new ServiceException("Administrator cannot delete!");
		}
		
		// check account data for other table use.
		this.checkInformationRelated(account, employee);
		
		this.deleteEmployeeOrganization(employee);
		
		// delete user role
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account.getAccount());
		List<TbUserRole> userRoles = this.getUserRoleService().findListByParams(params);
		for (int i=0; userRoles!=null && i<userRoles.size(); i++) {
			TbUserRole uRole = userRoles.get(i);
			this.getUserRoleService().delete(uRole);
		}
		
		// delete BB_REPORT_ROLE_VIEW
		params.clear();
		params.put("idName", account.getAccount());
		List<BbReportRoleView> reportRoleViews = this.reportRoleViewService.findListByParams(params);
		for (int i=0; reportRoleViews!=null && i<reportRoleViews.size(); i++) {
			BbReportRoleView reportRoleView = reportRoleViews.get( i );
			this.reportRoleViewService.delete(reportRoleView);
		}
		
		// delete from BB_MEASURE_DATA where EMP_ID = :empId
		this.measureDataService.deleteForEmpId( employee.getEmpId() );
		
		this.monitorItemScoreService.deleteForEmpId( employee.getEmpId() );
		
		this.getAccountService().deleteByPKng(account.getOid());
		return getEmployeeService().deleteObject(employee);
	}
	
	private void checkInformationRelated(AccountVO account, EmployeeVO employee) throws ServiceException, Exception {
		if (account==null || super.isBlank(account.getAccount()) 
				|| employee==null || super.isBlank(employee.getEmpId()) ) {
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		
		// tb_sys_msg_notice
		params.put("toAccount", account.getAccount());
		if (this.sysMsgNoticeService.countByParams(params) > 0 ) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.DATA_CANNOT_DELETE));
		}
		
		// tb_sys_calendar_note
		params.clear();
		params.put("account", account.getAccount());
		if (this.sysCalendarNoteService.countByParams(params) > 0 ) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.DATA_CANNOT_DELETE));
		}
		
		// bb_kpi_empl
		params.clear();
		params.put("empId", employee.getEmpId());
		if (this.kpiEmplService.countByParams(params) > 0) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.DATA_CANNOT_DELETE));
		}
		
		// bb_pdca_owner
		if (this.pdcaOwnerService.countByParams(params) > 0) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.DATA_CANNOT_DELETE));
		}
		
		// bb_pdca_item_owner
		if (this.pdcaItemOwnerService.countByParams(params) > 0) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.DATA_CANNOT_DELETE));
		}
		
		// bb_degree_feedback_assign
		params.clear();
		params.put("ownerId", employee.getEmpId());
		if (this.degreeFeedbackAssignService.countByParams(params) > 0) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.DATA_CANNOT_DELETE));
		}
		params.clear();
		params.put("raterId", employee.getEmpId());
		if (this.degreeFeedbackAssignService.countByParams(params) > 0) {
			throw new ServiceException(SysMessageUtil.get(GreenStepSysMsgConstants.DATA_CANNOT_DELETE));
		}
		
	}
	
	private void createEmployeeOrganization(EmployeeVO employee, List<String> organizationOid) throws ServiceException, Exception {
		if (employee==null || StringUtils.isBlank(employee.getEmpId()) 
				|| organizationOid==null || organizationOid.size() < 1 ) {
			return;
		}
		for (String oid : organizationOid) {
			OrganizationVO organization = this.findOrganizationData(oid);
			EmployeeOrgaVO empOrg = new EmployeeOrgaVO();
			empOrg.setEmpId(employee.getEmpId());
			empOrg.setOrgId(organization.getOrgId());
			this.employeeOrgaService.saveObject(empOrg);
		}		
	}
	
	private void deleteEmployeeOrganization(EmployeeVO employee) throws ServiceException, Exception {
		if (employee==null || StringUtils.isBlank(employee.getEmpId()) ) {
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("empId", employee.getEmpId());
		List<BbEmployeeOrga> searchList = this.employeeOrgaService.findListByParams(params);
		if (searchList==null || searchList.size()<1 ) {
			return;
		}
		for (BbEmployeeOrga empOrg : searchList) {
			this.employeeOrgaService.delete(empOrg);
		}		
	}

}
