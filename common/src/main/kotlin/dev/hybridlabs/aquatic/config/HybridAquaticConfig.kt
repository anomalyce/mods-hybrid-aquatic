package dev.hybridlabs.aquatic.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.concurrent.atomic.AtomicReference

data class HybridAquaticConfig(
    /**
     * The version of the data stored.
     * Increase when the config needs to be reset, i.e. when new entity spawn configs are added.
     */
    val dataVersion: Int = 5,

    val entitySpawnConfig: List<EntitySpawnConfig> = EntitySpawnConfigGenerator.generate(),
    val disableDeeperOceans: Boolean = false,
    val disableYetiCrabsAroundVents: Boolean = false
) {
    companion object {
        private val ref = AtomicReference(HybridAquaticConfig())

        @JvmStatic fun current(): HybridAquaticConfig = ref.get();
        @JvmStatic fun update(new: HybridAquaticConfig) { ref.set(new) }

        val CODEC: Codec<HybridAquaticConfig> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("data_version").forGetter(HybridAquaticConfig::dataVersion),
                EntitySpawnConfig.CODEC.listOf().fieldOf("spawn_configuration").forGetter(HybridAquaticConfig::entitySpawnConfig),
                Codec.BOOL.fieldOf("disable_ha_deeper_oceans").forGetter(HybridAquaticConfig::disableDeeperOceans),
                Codec.BOOL.fieldOf("disable_yeti_crabs_around_vents").forGetter(HybridAquaticConfig::disableYetiCrabsAroundVents)
            ).apply(instance, ::HybridAquaticConfig)
        }
    }
}
