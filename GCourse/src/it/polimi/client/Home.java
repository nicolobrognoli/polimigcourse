package it.polimi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;


public class Home implements EntryPoint {

	@Override
	public void onModuleLoad() {

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
		panel.addStyleName("table-center");
		
		rootLayoutPanel.add(panel);
		rootLayoutPanel.setWidgetLeftWidth(panel, 0.0, Unit.PX, 951.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(panel, 117.0, Unit.PX, 56.0, Unit.PX);
	}
}
