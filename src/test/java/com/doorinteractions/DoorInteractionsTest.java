package com.doorinteractions;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DoorInteractionsTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DoorInteractionsPlugin.class);
		RuneLite.main(args);
	}
}