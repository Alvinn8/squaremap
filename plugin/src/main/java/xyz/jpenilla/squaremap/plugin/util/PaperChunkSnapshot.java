package xyz.jpenilla.squaremap.plugin.util;

import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.common.util.ChunkSnapshot;

@DefaultQualifier(NonNull.class)
public final class PaperChunkSnapshot {
    private PaperChunkSnapshot() {
    }

    public static CompletableFuture<@Nullable ChunkSnapshot> asyncSnapshot(
        final ServerLevel level,
        final int x,
        final int z,
        final boolean biomesOnly
    ) {
        return level.getChunkSource().getChunkAtAsynchronously(x, z, false, true)
            .thenApply(either -> either.left()
                .map(chunk -> {
                    final LevelChunk levelChunk = (LevelChunk) chunk;
                    if (levelChunk.isEmpty()) {
                        return null;
                    }
                    return ChunkSnapshot.snapshot(levelChunk, biomesOnly);
                })
                .orElse(null));
    }
}
