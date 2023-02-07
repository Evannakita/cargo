package evannakita.cargo;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cargo implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("cargo");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}