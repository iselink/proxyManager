package net.iselink.proxymanager.connectivity.connections;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.IManagement;
import net.iselink.proxymanager.connectivity.messages.IMessageEventPublisher;
import net.iselink.proxymanager.connectivity.messages.IMessageExecute;
import net.iselink.proxymanager.connectivity.messages.Message;
import net.iselink.proxymanager.connectivity.messages.MessageResponse;
import net.md_5.bungee.api.plugin.Event;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class ManagementConnection implements IManagement {

	private final Queue<Message> transmisitonQueue = new ArrayBlockingQueue<>(100);    //TODO: hardcoded limit of queue
	private final Queue<Message> receivingQueue = new ArrayBlockingQueue<>(100);    //TODO: hardcoded limit of queue
	private final ProxyManagerPlugin proxyManagerPlugin;

	public ManagementConnection(ProxyManagerPlugin proxyManagerPlugin) {
		this.proxyManagerPlugin = proxyManagerPlugin;
	}

	protected Queue<Message> getTransmisitonQueue() {
		return transmisitonQueue;
	}

	protected Queue<Message> getReceivingQueue() {
		return receivingQueue;
	}

	public ProxyManagerPlugin getProxyManager() {
		return proxyManagerPlugin;
	}

	@Override
	public void processEvents() {
		//TODO: add processing limit if necessary
		while (!getReceivingQueue().isEmpty()) {
			Message msg = getReceivingQueue().poll();

			if (msg == null) {
				proxyManagerPlugin.getSLF4JLogger().debug("Pointless check saved the day.");
				return;
			}

			//if this kind of response *just* generate event, just throw event out and continue with next message
			if (msg instanceof IMessageEventPublisher) {
				Event event = ((IMessageEventPublisher) msg).createEvent();
				getProxyManager().getProxy().getPluginManager().callEvent(event);
				continue;
			}

			//if it is message requiring to execute a little bit of code, *just* execute it asap.
			if (msg instanceof IMessageExecute) {
				getProxyManager().getProxy().getScheduler().runAsync(getProxyManager(), () -> ((IMessageExecute) msg).execute(getProxyManager(), getProxyManager().getProxy(), this));
				continue;
			}


			if (!(msg instanceof MessageResponse)) {
				getProxyManager().getLogger().warning(String.format("Currently unprocessable message: %s - Message is dropped.", msg.getClass().getSimpleName()));
				continue;
			}

		}
	}
}
