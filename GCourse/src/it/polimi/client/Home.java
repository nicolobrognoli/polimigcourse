package it.polimi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
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
		String text1 = "Lorem ipsum dolor sit amet...";
		String text2 = "Sed egestas, arcu nec accumsan...";
		String text3 = "Proin tristique, elit at blandit...";

		TabPanel panel = new TabPanel();
		FlowPanel flowpanel;
		
		flowpanel = new FlowPanel();
		flowpanel.add(new Label(text1));
		panel.add(flowpanel, "One");

		flowpanel = new FlowPanel();
		flowpanel.add(new Label(text2));
		panel.add(flowpanel, "Two");

		flowpanel = new FlowPanel();
		flowpanel.add(new Label(text3));
		panel.add(flowpanel, "Three");

		panel.selectTab(0);
		panel.setWidth("100%");
		panel.addStyleName("table-center");
		
		rootLayoutPanel.add(panel);
	}
}
