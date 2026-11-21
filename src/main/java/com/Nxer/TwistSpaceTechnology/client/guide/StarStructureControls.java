package com.Nxer.TwistSpaceTechnology.client.guide;

import static com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarStructure.CHANNEL;
import static com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarStructure.FULL_PREVIEW_SECTION;

import java.util.Collections;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.lang3.StringUtils;

import com.Nxer.TwistSpaceTechnology.common.block.BlockCondensedStarCore;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.hfstudio.guidenh.config.ModConfig;
import com.hfstudio.guidenh.guide.compiler.PageCompiler;
import com.hfstudio.guidenh.guide.document.LytErrorSink;
import com.hfstudio.guidenh.guide.internal.scheduler.MasterScheduler;
import com.hfstudio.guidenh.guide.internal.scheduler.Priority;
import com.hfstudio.guidenh.guide.internal.scheduler.WorkItem;
import com.hfstudio.guidenh.guide.internal.scheduler.WorkResult;
import com.hfstudio.guidenh.guide.scene.CameraSettings;
import com.hfstudio.guidenh.guide.scene.LytGuidebookScene;
import com.hfstudio.guidenh.guide.scene.SceneViewportMetrics;
import com.hfstudio.guidenh.guide.scene.StructureLibSceneBinding;
import com.hfstudio.guidenh.guide.scene.annotation.compiler.AnnotationTagCompiler;
import com.hfstudio.guidenh.guide.scene.element.SceneElementTagCompiler;
import com.hfstudio.guidenh.guide.scene.level.GuidebookLevel;
import com.hfstudio.guidenh.guide.scene.level.GuidebookPreviewBlockPlacer;
import com.hfstudio.guidenh.guide.scene.preview.StructureLibDefinitionCache;
import com.hfstudio.guidenh.integration.api.GuideNhIntegrationRegistry;
import com.hfstudio.guidenh.integration.structurelib.StructureLibSceneMetadata;
import com.hfstudio.guidenh.libs.mdast.mdx.model.MdxJsxElementFields;

import cpw.mods.fml.common.Loader;

/** Adds the single-model overview to the star's native StructureLib channel slider. */
public final class StarStructureControls implements SceneElementTagCompiler {

    public static void register() {
        GuideNhIntegrationRegistry.global()
            .registerSceneElementTagCompilerProvider(compilers -> compilers.add(new StarStructureControls()));
    }

    @Override
    public Set<String> getTagNames() {
        return Collections.singleton("StarStructureControls");
    }

    @Override
    public void compile(GuidebookLevel level, CameraSettings camera, PageCompiler compiler, LytErrorSink errors,
        MdxJsxElementFields element) {
        LytGuidebookScene scene = AnnotationTagCompiler.CURRENT_SCENE.get();
        StructureLibSceneBinding binding = scene == null ? null : scene.resolveStructureLibBinding(null);
        if (binding == null || binding.getRebuildRequestTemplate() == null) {
            errors.appendError(
                compiler,
                "StarStructureControls requires the star's preceding ImportStructureLib.",
                element);
            return;
        }
        Sections controls = new Sections(scene, binding);
        // BR can publish channel ranges after the first book visit. This machine always declares all eight.
        controls.label(binding.getChannelValue(CHANNEL));
        // SceneScript builds the first section and installs its default listener after compiling all children.
        MasterScheduler.getInstance()
            .submit(controls);
    }

    private static final class Sections implements WorkItem {

        private final LytGuidebookScene scene;
        private final StructureLibSceneBinding binding;
        private boolean attached, overview, showStats, showLayers;

        Sections(LytGuidebookScene scene, StructureLibSceneBinding binding) {
            this.scene = scene;
            this.binding = binding;
        }

        @Override
        public Priority priority() {
            return Priority.HIGH;
        }

        @Override
        public boolean shouldRun() {
            return !attached || StructureLibDefinitionCache.getInstance()
                .getScanGeneration() > 0;
        }

        @Override
        public WorkResult tick(long deadlineNs) {
            if (!attached) {
                attached = true;
                scene.setStructureLibSelectionChangeListener(selection -> select(binding.getChannelValue(CHANNEL)));
            }
            // GuideNH republishes raw channel names when the optional BR scan finishes.
            scene.refreshStructureLibControlMetadata();
            label(binding.getChannelValue(CHANNEL));
            if (Loader.isModLoaded("blockrenderer6343") && StructureLibDefinitionCache.getInstance()
                .getScanGeneration() == 0) {
                return WorkResult.YIELD;
            }
            return WorkResult.DONE;
        }

        private void select(int section) {
            scene.setVisibleLayerSilently(0);
            if (section == FULL_PREVIEW_SECTION) {
                if (!overview) {
                    showStats = scene.isBlockStatsVisible();
                    showLayers = scene.isVisibleLayerSliderEnabled();
                }
                scene.setBlockStatsEnabled(false);
                scene.setVisibleLayerSliderEnabled(false);
                scene.clear();
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("tstGuideStar", true);
                GuidebookPreviewBlockPlacer.place(
                    scene.getLevel(),
                    0,
                    0,
                    0,
                    TstBlocks.CondensedStarCore,
                    BlockCondensedStarCore.GUIDE_PREVIEW_META,
                    tag,
                    null);
            } else {
                scene.rebuild();
                if (overview) {
                    scene.setBlockStatsEnabled(true);
                    scene.setBlockStatsButtonEnabled(ModConfig.ui.sceneBlockStatsButtonEnabled);
                    scene.setBlockStatsVisible(showStats);
                    scene.setVisibleLayerSliderEnabled(showLayers);
                }
            }
            overview = section == FULL_PREVIEW_SECTION;
            label(section);
            fitCamera();
        }

        private void label(int section) {
            var request = binding.getRebuildRequestTemplate();
            var metadata = binding.getMetadata();
            if (metadata == null || section == FULL_PREVIEW_SECTION) {
                metadata = new StructureLibSceneMetadata(
                    request.controllerId(),
                    request.piece(),
                    request.facing(),
                    request.rotation(),
                    request.flip());
            }
            String label = StringUtils.capitalize(CHANNEL);
            scene.setStructureLibSceneMetadata(
                binding.getName(),
                metadata.withChannelData(CHANNEL, label, FULL_PREVIEW_SECTION, section));
            scene.setStructureLibChannelValueSilently(binding.getName(), CHANNEL, section);
        }

        private void fitCamera() {
            var level = scene.getLevel();
            var camera = scene.getCamera();
            scene.setLevel(level);
            camera.setOffsetX(0);
            camera.setOffsetY(0);
            var viewport = camera.getViewportSize();
            float zoom;
            if (overview) {
                // The eight-unit proxy mesh rotates inside a sphere of radius 5.52; leave a margin on either side.
                zoom = Math.min(viewport.width(), viewport.height()) / 120f;
            } else {
                camera.setZoom(1f);
                var span = SceneViewportMetrics.measure(camera, level.getBounds());
                zoom = .85f * Math
                    .min(viewport.width() / Math.max(1f, span.spanX()), viewport.height() / Math.max(1f, span.spanY()));
            }
            camera.setZoom(Math.max(LytGuidebookScene.MIN_ZOOM, Math.min(LytGuidebookScene.MAX_ZOOM, zoom)));
            scene.snapshotInitialCamera();
            scene.syncVisualCameraToLogicalState();
        }
    }
}
