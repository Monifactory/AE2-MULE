package appeng.mixins.spatial;

import java.util.Map;
import java.util.concurrent.Executor;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import appeng.spatial.SpatialStorageChunkGenerator;
import appeng.spatial.SpatialStorageDimensionIds;

/**
 * Since our spatial dimension has to be available in EVERY world regardless of Worldgen settings or world preset, we
 * use a rather heavyhanded approach of directly adding a ServerLevel for it in MinecraftServer.
 * <p/>
 * Clients only ever receive a reference to the dimension type when they enter that dimension, they never get the full
 * Worldgen settings anyway (otherwise they could get the seeds). The dimension types are properly registered in
 * {@link appeng.init.worldgen.InitDimensionTypes} on both client and server.
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    private Map<ResourceKey<Level>, ServerLevel> levels;

    @Shadow
    protected WorldData worldData;

    @Shadow
    protected Executor executor;

    @Shadow
    protected LevelStorageSource.LevelStorageAccess storageSource;

    @Shadow
    protected LayeredRegistryAccess<RegistryLayer> registries;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "createLevels", at = @At(value = "TAIL"))
    public void injectSpatialLevel(ChunkProgressListener chunkProgressListener, CallbackInfo ci) {
        var registryHolder = registries.compositeAccess();

        var levelStem = new LevelStem(
                registryHolder.registryOrThrow(Registries.DIMENSION_TYPE)
                        .getHolderOrThrow(SpatialStorageDimensionIds.DIMENSION_TYPE_ID),
                new SpatialStorageChunkGenerator(
                        registryHolder.lookupOrThrow(Registries.BIOME)));

        long seed = BiomeManager.obfuscateSeed(this.worldData.worldGenOptions().seed());

        var serverLevelData = this.worldData.overworldData();
        var derivedLevelData = new DerivedLevelData(this.worldData, serverLevelData);
        var level = new ServerLevel(
                (MinecraftServer) (Object) this,
                this.executor,
                this.storageSource,
                derivedLevelData,
                SpatialStorageDimensionIds.WORLD_ID,
                levelStem,
                chunkProgressListener,
                false /* debug */,
                seed,
                ImmutableList.of(),
                false,
                null);
        // NOTE: We don't register the spatial dimension for the world-border. Players can't move freely in that
        // dimension anyway.
        this.levels.put(SpatialStorageDimensionIds.WORLD_ID, level);
        // Emulate the Forge world load event
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(level));
    }
}
