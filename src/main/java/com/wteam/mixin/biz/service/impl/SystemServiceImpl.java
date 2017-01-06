package com.wteam.mixin.biz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ISystemDao;
import com.wteam.mixin.biz.service.ISystemService;
import com.wteam.mixin.constant.DConfig;
import com.wteam.mixin.constant.Documents;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessMenuPo;
import com.wteam.mixin.model.po.DConfigPo;
import com.wteam.mixin.model.po.DocumentPo;
import com.wteam.mixin.model.vo.BusinessMenuVo;
import com.wteam.mixin.model.vo.DConfigVo;
import com.wteam.mixin.model.vo.DocumentVo;

@Service("systemService")
public class SystemServiceImpl implements ISystemService{

	@Autowired
	private IBaseDao baseDao;
	
	@Autowired
	ISystemDao systemDao;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public Map<String, String> getDConfig() {
		Map<String, String> map = new HashMap<>();
		
		DConfig.ALL.forEach(config -> {
		    map.put(config, systemDao.dconfig(config));
		});
		
        return map;
	}

	@Override
	public void updateDConfig(Map<String, String> configs) {
	    
	      DConfig.ALL.forEach(config -> {
	            if (configs.get(config) != null) {
                    systemDao.dconfig(config, configs.get(config));
                }
	      });
	}

	@Override
	public List<BusinessMenuVo> listBusinessMenu() {

		List<BusinessMenuPo> poLst = baseDao.find("from BusinessMenuPo");
		List<BusinessMenuVo> menuLst = poLst.stream()
				.map(po -> mapper.map(po, BusinessMenuVo.class))
				.collect(Collectors.toList());
		
		return menuLst;
	}

	@Override
	public void updateBusinessMenu(List<BusinessMenuVo> menus) {
		
		for (BusinessMenuVo menu : menus) {
			BusinessMenuPo _menu = baseDao.get("from BusinessMenuPo where menuKey=?", new Object[]{menu.getMenuKey()});
			if(_menu == null){
				_menu = mapper.map(menu, BusinessMenuPo.class);
				baseDao.save(_menu);
			} else {
			    mapper.map(menu, _menu);
			    baseDao.update(_menu);
			}
		}
	}

	@Override
	public List<DocumentVo> listDocName() {
		
		List<DocumentVo> docLst = baseDao.find("select new com.wteam.mixin.model.vo.DocumentVo(id, name) from DocumentPo");
		
		return docLst;
	}

	@Override
	public DocumentVo getDocByName(String name) {
		
		DocumentPo _document = baseDao.get("from DocumentPo where name=?", new Object[]{name});
		if(_document == null){
		    if (Documents.ALL.contains(name)) {
		        _document = new DocumentPo(name, null, "");
		        baseDao.save(_document);
            }
            else {
                throw new ServiceException("该文档不存在");
            }
		}
		
		return new DocumentVo(name, _document.getDescription(), _document.getContent());
	}

	@Override
	public void updateDoc(DocumentVo doc) {
		
		DocumentPo _document = baseDao.get("from DocumentPo where name=?", new Object[]{doc.getName()});
		if(_document == null){
            if (Documents.ALL.contains(doc.getName())) {
                _document = mapper.map(doc, DocumentPo.class);
                baseDao.save(_document);
            }
            else {
                throw new ServiceException("该文档不存在");
            }
		}else {
		    mapper.map(doc, _document);
		    baseDao.update(_document);
        }
	}
}








