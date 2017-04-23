package models.nodes;

import models.Feature;
import models.Manufacturer;
import models.Role;
import models.Search;
import models.TimeToLive;

public class SlaveNode extends Node {
	private MasterNode master;
	
	public SlaveNode(Integer id, Manufacturer manufacturer, Role role, 
			TimeToLive timeToLive) {
		super(id, manufacturer, role, timeToLive);
	}
	
	@Override
	public Search discover(Integer feature) {
		return master.discover(feature);
	}

	public MasterNode getMaster() {
		return master;
	}

	public void setMaster(MasterNode master) {
		this.master = master;
	}	
}
