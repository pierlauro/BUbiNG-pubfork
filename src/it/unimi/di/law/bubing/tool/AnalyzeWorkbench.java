package it.unimi.di.law.bubing.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.util.function.LongConsumer;

import it.unimi.di.law.bubing.frontier.VisitState;
import it.unimi.di.law.bubing.util.Util;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import com.google.common.primitives.Ints;

public class AnalyzeWorkbench {

	@SuppressWarnings("boxing")
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		final ObjectInputStream workbenchStream = new ObjectInputStream(new FastBufferedInputStream(new FileInputStream(new File(args[0]))));

		final long workbenchSize = workbenchStream.readLong();
		long w = workbenchSize;
		Int2LongOpenHashMap ip2count = new Int2LongOpenHashMap();
		ip2count.defaultReturnValue(0);
		while(w-- != 0) {
			final VisitState visitState = (VisitState)workbenchStream.readObject();
			final boolean nonNullWorkbenchEntry = workbenchStream.readBoolean();
			if (nonNullWorkbenchEntry) {
				byte[] ipAddress = Util.readByteArray(workbenchStream);
				ip2count.addTo(Ints.fromByteArray(ipAddress), 1);
				System.err.print(new String(visitState.schemeAuthority, StandardCharsets.US_ASCII));
				System.err.print('\t');
				System.err.println(Inet4Address.getByAddress(ipAddress));
			}
		}
		
		Long2LongOpenHashMap count2Freq = new Long2LongOpenHashMap();
		count2Freq.defaultReturnValue(0);
		ip2count.values().forEach((LongConsumer)(x -> { count2Freq.addTo(x, 1); }));
		count2Freq.forEach((x,y)-> { System.out.println(x + "\t" + y); });
	}
}
