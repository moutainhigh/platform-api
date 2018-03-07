package com.xinleju.platform.sys.num.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.base.utils.SortType;
import com.xinleju.platform.sys.num.dao.RulerDao;
import com.xinleju.platform.sys.num.entity.Ruler;
import com.xinleju.platform.sys.num.service.RulerService;

/**
 * @author admin
 * 
 * 
 */
@Service
public class RulerServiceImpl extends  BaseServiceImpl<String,Ruler> implements RulerService{
	

	@Autowired
	private RulerDao rulerDao;

	@Override
	public List<Map<String, Object>> getMapListByBillId(String paramater)
			throws Exception {
	
		return rulerDao.getMapListByBillId(paramater);
	}
	
	@Override
	public List<Ruler> getRuleListByBillId(String paramater)
			throws Exception {
		
		return rulerDao.getRuleListByBillId(paramater);
	}

	@Override
	public List<Map<String, Object>> getRulerSortNum(Map<String,Object> paramater) throws Exception {
		return rulerDao.getRulerSortNum(paramater);
	}

	/**
	 * @author lY
	 * 操作数据排列顺序     上移1 下移2 置顶 3 置底4
	 */
	@Override
	public synchronized int updateSort(Ruler object, Map<String, Object> map) throws Exception {
		 String  sortType= String.valueOf(map.get("sortType"));
			Integer sort1 = object.getSort();
			String billId = object.getBillId();
			List<Ruler> rulerList = rulerDao.getRuleListByBillId(billId);
			if(SortType.SHIFTUP.getCode().equals(sortType)){
				for (int i = 0; i < rulerList.size(); i++) {
					Integer sort2 = rulerList.get(i).getSort();
					if(sort2==sort1&&i!=0){
						Integer sort3 = rulerList.get(i-1).getSort();
						rulerList.get(i-1).setSort(sort2);
						rulerList.get(i).setSort(sort3);
						rulerDao.update(rulerList.get(i-1));
						rulerDao.update(rulerList.get(i));
						break;
					}
				}
			}else if(SortType.SHIFTDOWN.getCode().equals(sortType)){
			  for (int i = 0; i < rulerList.size(); i++) {
					Integer sort2 = rulerList.get(i).getSort();
					if(sort2==sort1&&i!=rulerList.size()-1){
						Integer sort3 = rulerList.get(i+1).getSort();
						rulerList.get(i+1).setSort(sort2);
						rulerList.get(i).setSort(sort3);
						rulerDao.update(rulerList.get(i+1));
						rulerDao.update(rulerList.get(i));	
						break;
					}
			}
			}else if(SortType.STICK.getCode().equals(sortType)){
				int len = rulerList.size()-1;
				Integer num =  sort1;
				int isGo = 0;
				for (int i = len; i >0 ; i--) {
					if(rulerList.get(i).getSort()==sort1){
						isGo = i;
						rulerList.get(i).setSort(rulerList.get(0).getSort());
						rulerDao.update(rulerList.get(i));
					}
					if(isGo>0){
						rulerList.get(i).setSort(num);
						num = rulerList.get(i-1).getSort();
						rulerList.get(i-1).setSort(rulerList.get(i).getSort());
						rulerDao.update(rulerList.get(i-1));
					}
				}
			}else if(SortType.TOBOTTOM.getCode().equals(sortType)){
				int len = rulerList.size()-1;
				Integer num =  sort1;
				int isGo = -1;
				for (int i = 0; i <len ; i++) {
					if(rulerList.get(i).getSort()==sort1){
						isGo = i;
						rulerList.get(i).setSort(rulerList.get(len).getSort());
						rulerDao.update(rulerList.get(i));
					}
					if(isGo>-1){
						rulerList.get(i).setSort(num);
						num = rulerList.get(i+1).getSort();
						rulerList.get(i+1).setSort(rulerList.get(i).getSort());
						rulerDao.update(rulerList.get(i+1));
					}
				}
			}
			return 1;
	}
	

}
