package com.wteam.mixin.hibernate.link;

import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;

public class LinkerFactory {
	
	public final ILinker<UserPo, RolePo> userToRole = new ILinker<UserPo, RolePo>() {
		@Override
		public void link(UserPo t, RolePo s) {
			t.getRoles().add(s);
			s.getUsers().add(t);
		}
	};

}
