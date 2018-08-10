package org.cmucreatelab.flutter_android;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsChange;
import org.cmucreatelab.flutter_android.classes.settings.SettingsCumulative;
import org.cmucreatelab.flutter_android.classes.settings.SettingsFrequency;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class FlutterVersionOneProtocolTest
{
	Servo servo;
	Flutter flutter;

	@Test
	public void proportionalServoControl_isWorking() throws Exception
	{
		flutter = new Flutter(null, "");
		servo = flutter.getServos()[0];
		SettingsProportional settingsProportional = SettingsProportional.newInstance(flutter);
		settingsProportional.setOutputMin(0);
		settingsProportional.setOutputMax(180);
		settingsProportional.setSensorPortNumber(1);
		settingsProportional.getAdvancedSettings().setVoltageMin(0);
		settingsProportional.getAdvancedSettings().setVoltageMax(100);
		String proportionalControlMessage = MessageConstructor.constructRelationshipMessage(servo, settingsProportional).getRequests().get(0);
		assertEquals(proportionalControlMessage, "ps1,0,b4,1,0,64");
	}

	@Test
	public void amplitudeServoControl_isWorking() throws Exception
	{
		flutter = new Flutter(null, "");
		servo = flutter.getServos()[0];
		SettingsAmplitude settingsAmplitude = SettingsAmplitude.newInstance(flutter);
		settingsAmplitude.setOutputMin(45);
		settingsAmplitude.setOutputMax(135);
		settingsAmplitude.setSensorPortNumber(1);
		settingsAmplitude.getAdvancedSettings().setVoltageMin(0);
		settingsAmplitude.getAdvancedSettings().setVoltageMax(100);
		settingsAmplitude.getAdvancedSettings().setSpeed(5);
		String amplitudeControlMessage = MessageConstructor.constructRelationshipMessage(servo, settingsAmplitude).getRequests().get(0);
		assertEquals(amplitudeControlMessage, "as1,2d,87,1,0,64,5");
	}

	@Test
	public void frequencyServoControl_isWorking() throws Exception
	{
		flutter = new Flutter(null, "");
		servo = flutter.getServos()[0];
		SettingsFrequency settingsFrequency = SettingsFrequency.newInstance(flutter);
		settingsFrequency.setOutputMin(0);
		settingsFrequency.setOutputMax(180);
		settingsFrequency.setSensorPortNumber(1);
		settingsFrequency.getAdvancedSettings().setVoltageMin(0);
		settingsFrequency.getAdvancedSettings().setVoltageMax(100);
		String proportionalControlMessage = MessageConstructor.constructRelationshipMessage(servo, settingsFrequency).getRequests().get(0);
		assertEquals(proportionalControlMessage, "fs1,0,b4,1,0,64");
	}

	@Test
	public void changeServoControl_isWorking() throws Exception
	{
		flutter = new Flutter(null, "");
		servo = flutter.getServos()[0];
		SettingsChange settingsChange = SettingsChange.newInstance(flutter);
		settingsChange.setOutputMin(0);
		settingsChange.setOutputMax(180);
		settingsChange.setSensorPortNumber(1);
		settingsChange.getAdvancedSettings().setVoltageMin(50);
		settingsChange.getAdvancedSettings().setVoltageMax(100);
		String changeControlMessage = MessageConstructor.constructRelationshipMessage(servo, settingsChange).getRequests().get(0);
		assertEquals(changeControlMessage, "ds1,0,b4,1,32,64");
	}

	@Test
	public void cumulativeServoControl_isWorking() throws Exception
	{
		flutter = new Flutter(null, "");
		servo = flutter.getServos()[0];
		SettingsCumulative settingsCumulative = SettingsCumulative.newInstance(flutter);
		settingsCumulative.setOutputMin(0);
		settingsCumulative.setOutputMax(180);
		settingsCumulative.setSensorPortNumber(1);
		settingsCumulative.getAdvancedSettings().setVoltageMin(50);
		settingsCumulative.getAdvancedSettings().setVoltageMax(100);
		String cumulativeControlMessage = MessageConstructor.constructRelationshipMessage(servo, settingsCumulative).getRequests().get(0);
		assertEquals(cumulativeControlMessage, "is1,0,b4,1,32,64,0,0");
	}
}