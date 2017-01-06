package test.hibernate.factory;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dozer.DozerBeanMapper;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.constant.PlatformInfo;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessBannerPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessMenuPo;
import com.wteam.mixin.model.po.BusinessThemePo;
import com.wteam.mixin.model.po.BusinessTrafficPlanPo;
import com.wteam.mixin.model.po.DConfigPo;
import com.wteam.mixin.model.po.DocumentPo;
import com.wteam.mixin.model.po.MemberPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.MemberVaildityPo;
import com.wteam.mixin.model.po.PlatformInfoPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.ThemePo;
import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.po.WechatCustomMenuPo;
import com.wteam.mixin.model.po.WechatReplyPo;
import com.wteam.mixin.model.po.WithdrawPo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.shiro.PasswordHelper;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.generate.MockGenerate;


public class GWWMockGenerateFactory {

    public final MockGenerate<UserPo> user;

    public final MockGenerate<RolePo> role;
    
    public final MockGenerate<BusinessBannerPo> businessBanner;
    
    public final MockGenerate<MemberPo> member;
    
    public final MockGenerate<MemberTrafficPlanPo> memPlan;
    
    public final MockGenerate<MemberVaildityPo> vail;
    
    public final MockGenerate<BusinessInfoPo> busi;
    
    public final MockGenerate<ThemePo> themeLst;
    
    public final MockGenerate<BusinessThemePo> busiThemeLst;
    
    public final MockGenerate<TrafficGroupPo> group;
    
    public final MockGenerate<TrafficPlanPo> plan;
    
    public final MockGenerate<BusinessTrafficPlanPo> busiPlan;
    
    public final MockGenerate<PlatformInfoPo> platromInfo;
    
    public final MockGenerate<BusinessBalanceRecordPo> balance;
    
    public final MockGenerate<WithdrawPo> withdraw;
    
    public final MockGenerate<DConfigPo> config;
    
    public final MockGenerate<BusinessMenuPo> menu;
    
    public final MockGenerate<DocumentPo> doc;
    
    public final MockGenerate<WechatReplyPo> reply;
    
    public final MockGenerate<WechatCustomMenuPo> cMenu;
    
    public GWWMockGenerateFactory(IBaseDao baseDao, DozerBeanMapper mapper) {

        user = new MockGenerate<UserPo>(baseDao) {

            private PasswordHelper helper;

            @Override
            protected void setList(List<UserPo> list) {
                helper = new PasswordHelper();
                List<UserVo> voList = new ArrayList<>();
                
                voList.add(new UserVo("super", MD5Utils.md5("000000"), "18320372265", "933651027@qq.com"));
                voList.add(new UserVo("business", MD5Utils.md5("000001"), "18320376671", "9321410271@qq.com"));
                voList.add(new UserVo("user", MD5Utils.md5("000002"), "18320376672", "932141027@qq.com"));
                
                for (UserVo vo : voList) {
                    encryptUser(vo);
                    list.add(mapper.map(vo, UserPo.class));
                }
            }

            private void encryptUser(UserVo userVo) {
                String uuid;
                uuid = UUID.randomUUID().toString().replaceAll("-", "");
                userVo.setPrincipal(uuid);
                helper.encryptPassword(userVo);
            }

        };

        role = new MockGenerate<RolePo>(baseDao) {
            @Override
            protected void setList(List<RolePo> list) {
                for (RoleType role : RoleType.values()) {
                    list.add(new RolePo(role));
                }
            }
        };
        
        businessBanner = new MockGenerate<BusinessBannerPo>(baseDao) {

			@Override
			protected void setList(List<BusinessBannerPo> list) {
//				for (int i = 0; i < 5; i++) {
					list.add(new BusinessBannerPo(1111L, "gg" , 1L, false, "sdfasdf"));
//				}
//				for (int i = 6; i < 10; i++) {
//					list.add(new businessBannerPo(222L+i, "gg" + i, 1L, false, "sdfasdf"));
//				}
				
			}
		};
		
		member = new MockGenerate<MemberPo>(baseDao) {
			
			@Override
			protected void setList(List<MemberPo> list) {
				MemberPo member = new MemberPo("砖石会员", "", "没错拥有砖头打脸特权");
//				member.setId(123456L);
				list.add(member);
				list.add(new MemberPo("黄金会员", "", "拥有化腐朽为黄金(粑粑)的能力- -"));
				list.add(new MemberPo("白银会员", "", "见面送白银"));
				list.add(new MemberPo("什么也不是会员", "", "当然什么也没有啦"));
			}
		};
		
		memPlan = new MockGenerate<MemberTrafficPlanPo>(baseDao) {
			@Override
			protected void setList(List<MemberTrafficPlanPo> list) {
				list.add(new MemberTrafficPlanPo(12356L, 12356L,new BigDecimal(2500L)));
				list.add(new MemberTrafficPlanPo(12356L, 12356L,new BigDecimal(5000L)));
				list.add(new MemberTrafficPlanPo(12356L, 12356L,new BigDecimal(25412L)));
				list.add(new MemberTrafficPlanPo(12356L, 12356L,new BigDecimal(45465L)));
			}
		};
		
		vail = new MockGenerate<MemberVaildityPo>(baseDao) {
			@Override
			protected void setList(List<MemberVaildityPo> list) {
				//砖石会员
				list.add(new MemberVaildityPo(123456L, new BigDecimal(20000L), 30));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(30000L), 90));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(40000L), 180));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(50000L), 360));
				//黄金会员
				list.add(new MemberVaildityPo(123456L,new BigDecimal(2000L), 30));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(3000L), 90));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(4000L), 180));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(5000L), 360));
				//白银会员
				list.add(new MemberVaildityPo(123456L,new BigDecimal(200L), 30));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(300L), 90));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(400L), 180));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(500L), 360));
				//什么也不是会员
				list.add(new MemberVaildityPo(123456L,new BigDecimal(20L), 30));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(30L), 90));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(40L), 180));
				list.add(new MemberVaildityPo(123456L,new BigDecimal(50L), 360));
			}
		};
		
		busi = new MockGenerate<BusinessInfoPo>(baseDao) {
			@Override
			protected void setList(List<BusinessInfoPo> list) {
				list.add(new BusinessInfoPo(123456L, new BigDecimal(0L), new Date(), 0));//余额不足
				list.add(new BusinessInfoPo(123456L, new BigDecimal(99999L), new Date(), 0));//未购买过同类产品
				list.add(new BusinessInfoPo(123456L, new BigDecimal(99999L), new Date(), 30));
				list.add(new BusinessInfoPo(123456L, new BigDecimal(99999L), new Date(), -1));//购买的是相同产品，已过期
				list.add(new BusinessInfoPo(123456L, new BigDecimal(99999L), new Date(), 20));//购买的不是相同产品
				list.add(new BusinessInfoPo(123456L,"wxcea32222d6f7c2ed", "Z0D_IKWMhw0DQJx2_UIse0mynQW_yGvvaaHooHgUTVegZgLaJDdr_r15YkHJBDisKSSephTpxqVQOekzGtnz_pAYoEIyvBQTWn6c9KikXDcHQHTfwPgdKgZWylSHYYbKNULdAIDCJA", 
						7200*1000L, new Date(), "MS3WVz6ZLVblYlT_WcZm-AqmOADY6jr0w8DADObayfQ"));
			}
		};
		
		themeLst = new MockGenerate<ThemePo>(baseDao) {
			@Override
			protected void setList(List<ThemePo> list) {
				list.add(new ThemePo("主题A", "no sign", 1L, false, new BigDecimal(0L), 999, false, true));
				list.add(new ThemePo("主题B", "no sign", 1L, true,  new BigDecimal(3000L), 30, true, true));
				list.add(new ThemePo("主题C", "no sign", 1L, false, new BigDecimal(99999L), 60, false, true));
				list.add(new ThemePo("主题D", "no sign", 1L, false, new BigDecimal(3000L), -1, true, true));
				list.add(new ThemePo("主题E", "no sign", 1L, false, new BigDecimal(3000L), 30, true, true));
				list.add(new ThemePo("主题F", "no sign", 1L, false, new BigDecimal(8880L), 360, true, true));
				list.add(new ThemePo("主题G", "no sign", 1L, false, new BigDecimal(0L), 180, false, true));
				list.add(new ThemePo("主题X", "no sign", 1L, false, new BigDecimal(3000L), 7, true,  true));
				list.add(new ThemePo("主题Y", "no sign", 1L, false, new BigDecimal(3000L), 15, false,  true));
				list.add(new ThemePo("主题Z", "no sign", 1L, false, new BigDecimal(3000L), 999, false, false));
			}
		};
		
		busiThemeLst = new MockGenerate<BusinessThemePo>(baseDao) {
			@Override
			protected void setList(List<BusinessThemePo> list) {
				long dayMiliseconds = 24*60*60*1000L;
				long current = new Date().getTime();
				list.add(new BusinessThemePo(123456L, new Date(current-(dayMiliseconds*1)), true, 30));
				list.add(new BusinessThemePo(123456L, new Date(current-(dayMiliseconds*21)), false, 20));
				for(int i=0;i<3;i++){
					list.add(new BusinessThemePo(123456L, new Date(current-(dayMiliseconds*(i+1)*2)), false, (i+1)*20));
				}
			}
		};
		
		group = new MockGenerate<TrafficGroupPo>(baseDao) {
			@Override
			protected void setList(List<TrafficGroupPo> list) {
				
				list.add(new TrafficGroupPo("动感地带", 0, "广东", "广东移动动感地带A", true, 0, false));
				list.add(new TrafficGroupPo("动感地带", 0, "广东", "广东移动动感地带B", false, 0, false));
				list.add(new TrafficGroupPo("全球通", 0, "上海", "上海移动全球通", false, 0, false));
				list.add(new TrafficGroupPo("神州行", 0, "云南", "云南移动神州行", true, 0, false));
				list.add(new TrafficGroupPo("天翼什么鬼", 1, "广东", "广东电信天翼", false, 0, false));
				list.add(new TrafficGroupPo("我的e家", 1, "湖北", "湖北电信我的e家", true, 0, false));
				list.add(new TrafficGroupPo("ChinaNet", 1, "全国", "全国电信ChinaNet", true, 0, false));
				list.add(new TrafficGroupPo("联通什么的", 2, "湖南", "没名字", true, 0, false));
			}
		};
		
		plan = new MockGenerate<TrafficPlanPo>(baseDao) {
			@Override
			protected void setList(List<TrafficPlanPo> list) {
				list.add(new TrafficPlanPo("广东移动", "30M", new BigDecimal(30l), new BigDecimal(32l), true, 0, "A"));
				list.add(new TrafficPlanPo("广东移动", "100M", new BigDecimal(100l), new BigDecimal(128l), true, 0, "A"));
				list.add(new TrafficPlanPo("广东移动", "300M", new BigDecimal(300l), new BigDecimal(256l), true, 0, "A"));
				list.add(new TrafficPlanPo("全国移动", "500M", new BigDecimal(500l), new BigDecimal(512l), true, 0, "A"));
				list.add(new TrafficPlanPo("全国电信", "1G", new BigDecimal(1000l), new BigDecimal(1024l), false, 1, "B"));
				list.add(new TrafficPlanPo("全国电信", "2G", new BigDecimal(2000l), new BigDecimal(2048l), false, 1, "B"));
				list.add(new TrafficPlanPo("广东电信", "5G", new BigDecimal(5000l), new BigDecimal(4096l), true, 1, "B"));
				list.add(new TrafficPlanPo("湖南联通", "10G", new BigDecimal(10000l), new BigDecimal(8192l), true, 2, "C"));
				list.add(new TrafficPlanPo("全国联通", "20G", new BigDecimal(20000l), new BigDecimal(16384l), true, 2, "C"));
			}
		};
		
		busiPlan = new MockGenerate<BusinessTrafficPlanPo>(baseDao) {
			@Override
			protected void setList(List<BusinessTrafficPlanPo> list) {
				BusinessTrafficPlanPo busiPlan = new BusinessTrafficPlanPo();
				busiPlan.setBusinessId(123456L);
				busiPlan.setTrafficplanId(123456L);
				busiPlan.setTip("GGG");
				busiPlan.setRetailPrice(new BigDecimal(99999));
				list.add(busiPlan);
			}
		};
		
		platromInfo = new MockGenerate<PlatformInfoPo>(baseDao) {
			@Override
			protected void setList(List<PlatformInfoPo> list) {
				list.add(new PlatformInfoPo(PlatformInfo.Balance, "11111"));
				list.add(new PlatformInfoPo(PlatformInfo.Profits, "22222"));
				list.add(new PlatformInfoPo(PlatformInfo.OrderCost, "33333"));
				list.add(new PlatformInfoPo(PlatformInfo.OrderIncome, "44444"));
				list.add(new PlatformInfoPo(PlatformInfo.Settlement, "55555"));
				list.add(new PlatformInfoPo(PlatformInfo.UnSettlement, "66666"));
			}
		};
		
		balance = new MockGenerate<BusinessBalanceRecordPo>(baseDao) {
			@Override
			protected void setList(List<BusinessBalanceRecordPo> list) {
				list.add(new BusinessBalanceRecordPo(123L, new BigDecimal(-1111L), State.BBRecordSource.service));
				list.add(new BusinessBalanceRecordPo(123L, new BigDecimal(-1111L), State.BBRecordSource.withdraw));
				list.add(new BusinessBalanceRecordPo(123L, new BigDecimal(-12654L), State.BBRecordSource.withdraw));
				list.add(new BusinessBalanceRecordPo(123L, new BigDecimal(-1111L), State.BBRecordSource.service));
				list.add(new BusinessBalanceRecordPo(123L, new BigDecimal(99999L), State.BBRecordSource.balanceRechange));
				list.add(new BusinessBalanceRecordPo(123L, new BigDecimal(-1111L), State.BBRecordSource.service));
				list.add(new BusinessBalanceRecordPo(456L, new BigDecimal(-1111L), State.BBRecordSource.service));
				list.add(new BusinessBalanceRecordPo(456L, new BigDecimal(1234L), State.BBRecordSource.settlement));
				list.add(new BusinessBalanceRecordPo(456L, new BigDecimal(-1111L), State.BBRecordSource.service));
				list.add(new BusinessBalanceRecordPo(456L, new BigDecimal(-4678L), State.BBRecordSource.orderCost));
				list.add(new BusinessBalanceRecordPo(456L, new BigDecimal(-1111L), State.BBRecordSource.service));
			}
		};
		
		withdraw = new MockGenerate<WithdrawPo>(baseDao) {
			@Override
			protected void setList(List<WithdrawPo> list) {
				list.add(new WithdrawPo(123456L, "18322266666", new BigDecimal(9999L), "高利贷", State.WithdrawState.successed));
				list.add(new WithdrawPo(123456L, "18322266666", new BigDecimal(6666L), "AAA", State.WithdrawState.failed));
				list.add(new WithdrawPo(123456L, "18322266666", new BigDecimal(13456L), "BBBB", State.WithdrawState.wait4audit));
				list.add(new WithdrawPo(123456L, "18322266666", new BigDecimal(2586L), "CCCC", State.WithdrawState.wait4audit));
				list.add(new WithdrawPo(123456L, "18322266666", new BigDecimal(1259L), "DDDD", State.WithdrawState.successed));
				list.add(new WithdrawPo(123456L, "18322266666", new BigDecimal(9527L), "高利贷", State.WithdrawState.successed));
			}
		};
		
		config = new MockGenerate<DConfigPo>(baseDao) {
			@Override
			protected void setList(List<DConfigPo> list) {
		
				list.add(new DConfigPo("CustomerOrderRate", "0.05"));
				list.add(new DConfigPo("WithdrawMinPrice", "20000"));
			}
		};
		
		menu = new MockGenerate<BusinessMenuPo>(baseDao) {
			@Override
			protected void setList(List<BusinessMenuPo> list) {
				list.add(new BusinessMenuPo("a---1", "abc", "new"));
				list.add(new BusinessMenuPo("a---2", "asd", "hot"));
				list.add(new BusinessMenuPo("a---3", "abasc", ""));
				list.add(new BusinessMenuPo("a---4", "asda", ""));
			}
		};
		
		doc = new MockGenerate<DocumentPo>(baseDao) {
			@Override
			protected void setList(List<DocumentPo> list) {
				list.add(new DocumentPo("卜算子", "苏轼", "缺月挂疏桐，漏断人初静。谁见幽人独往来，缥缈孤鸿影。惊起却回头，有恨无人省。拣尽寒枝不肯栖，寂寞沙洲冷。"));
				list.add(new DocumentPo("春江花月夜", "张若虚", "春江潮水连海平，海上明月共潮生。滟滟随波千万里，何处春江无月明！江流宛转绕芳甸，月照花林皆似霰;空里流霜不觉飞，汀上白沙看不见。江天一色无纤尘，皎皎空中孤月轮。江畔何人初见月？江月何年初照人？人生代代无穷已，江月年年只相似。不知江月待何人，但见长江送流水。白云一片去悠悠，青枫浦上不胜愁。谁家今夜扁舟子？何处相思明月楼？可怜楼上月徘徊，应照离人妆镜台。玉户帘中卷不去，捣衣砧上拂还来。此时相望不相闻，愿逐月华流照君。鸿雁长飞光不度，鱼龙潜跃水成文。昨夜闲潭梦落花，可怜春半不还家。江水流春去欲尽，江潭落月复西斜。斜月沉沉藏海雾，碣石潇湘无限路。不知乘月几人归，落月摇情满江树。"));
			}
		};
		
		reply = new MockGenerate<WechatReplyPo>(baseDao) {
			@Override
			protected void setList(List<WechatReplyPo> list){
				list.add(new WechatReplyPo(123456L, "ABC", "ABC", true));
				list.add(new WechatReplyPo(123456L, "MM", "ABC", true));
				list.add(new WechatReplyPo(123456L, "BB", "ABC", true));
				list.add(new WechatReplyPo(123456L, "CC", "ABC", true));
				list.add(new WechatReplyPo(123456L, "DDD", "ABC", true));
			}
		};
		
		cMenu = new MockGenerate<WechatCustomMenuPo>(baseDao) {
			@Override
			protected void setList(List<WechatCustomMenuPo> list){
				list.add(new WechatCustomMenuPo(123456L, "关注", null, null, null, 1, 0L));
				list.add(new WechatCustomMenuPo(123456L, "今日关注", null, "http://www.home.com", "view", 2, 123456L));
				list.add(new WechatCustomMenuPo(123456L, "昨天关注", null, "http://www.home.com", "view", 2, 123456L));
				list.add(new WechatCustomMenuPo(123456L, "明天关注", null, "http://www.home.com", "view", 2, 123456L));
				
				list.add(new WechatCustomMenuPo(123456L, "新闻", null, null, null, 1, 0L));
				list.add(new WechatCustomMenuPo(123456L, "体育新闻", null, "http://www.home.com", "view", 2, 123456L));
				list.add(new WechatCustomMenuPo(123456L, "财经新闻", null, "http://www.home.com", "view", 2, 123456L));
			}
		};
    }
    
    public static void main(String[] args) {
        System.out.println(MD5Utils.md5("000001"));// 4fc711301f3c784d66955d98d399afb
        System.out.println(MD5Utils.md5("000002"));// 768c1c687efe184ae6dd2420710b8799
    }

}
