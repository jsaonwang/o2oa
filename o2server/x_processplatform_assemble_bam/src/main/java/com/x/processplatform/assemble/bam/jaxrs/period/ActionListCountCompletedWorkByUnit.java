package com.x.processplatform.assemble.bam.jaxrs.period;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.DateRange;
import com.x.base.core.project.tools.DateTools;
import com.x.processplatform.assemble.bam.Business;
import com.x.processplatform.assemble.bam.ThisApplication;
import com.x.processplatform.assemble.bam.stub.UnitStub;

class ActionListCountCompletedWorkByUnit extends BaseAction {
	
	private static Logger logger = LoggerFactory.getLogger(ActionListCountCompletedWorkByUnit.class);

	ActionResult<Wo> execute(String applicationId, String processId) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Business business = new Business(emc);
			List<DateRange> os = this.listDateRange();
			Wo wo = new Wo();
			for (DateRange o : os) {
				String str = DateTools.format(o.getStart(), DateTools.format_yyyyMM);
				List<WoUnit> list = new ArrayList<>();
				wo.put(str, list);
				for (UnitStub stub : ThisApplication.period.getCompletedWorkUnitStubs()) {
					WoUnit w = new WoUnit();
					w.setName(stub.getName());
					w.setValue(stub.getValue());
					w.setLevel(stub.getLevel());
					Long count = this.countCompletedWork(business, o, applicationId, processId,
							StandardJaxrsAction.EMPTY_SYMBOL, StandardJaxrsAction.EMPTY_SYMBOL);
					w.setCount(count);
					Long duration = this.durationCompletedWork(business, o, applicationId, processId,
							StandardJaxrsAction.EMPTY_SYMBOL, StandardJaxrsAction.EMPTY_SYMBOL);
					w.setDuration(duration);
					Long times = this.timesCompletedWork(business, o, applicationId, processId,
							StandardJaxrsAction.EMPTY_SYMBOL, StandardJaxrsAction.EMPTY_SYMBOL);
					w.setTimes(times);
					list.add(w);
				}
			}
			result.setData(wo);
			return result;
		}
	}

	public static class Wo extends LinkedHashMap<String, List<WoUnit>> {

		private static final long serialVersionUID = -8039272843914575173L;

	}

	public static class WoUnit extends GsonPropertyObject {
		private String name;
		private String value;
		private Integer level;
		private Long count;
		private Long duration;
		private Long times;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Integer getLevel() {
			return level;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}

		public Long getCount() {
			return count;
		}

		public void setCount(Long count) {
			this.count = count;
		}

		public Long getDuration() {
			return duration;
		}

		public void setDuration(Long duration) {
			this.duration = duration;
		}

		public Long getTimes() {
			return times;
		}

		public void setTimes(Long times) {
			this.times = times;
		}
	}

}