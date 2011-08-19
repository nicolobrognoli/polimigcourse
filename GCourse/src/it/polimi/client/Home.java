package it.polimi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RadioButton;

public class Home implements EntryPoint {

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		rootLayoutPanel.add(horizontalPanel);
		rootLayoutPanel.setWidgetLeftWidth(horizontalPanel, 0.0, Unit.PX, 645.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(horizontalPanel, 0.0, Unit.PX, 490.0, Unit.PX);
		
		DecoratedTabPanel decoratedTabPanel = new DecoratedTabPanel();
		horizontalPanel.add(decoratedTabPanel);
		
		DecoratedTabPanel decoratedTabPanel_1 = new DecoratedTabPanel();
		horizontalPanel.add(decoratedTabPanel_1);
		
		DecoratedTabPanel decoratedTabPanel_2 = new DecoratedTabPanel();
		horizontalPanel.add(decoratedTabPanel_2);
	}
}
