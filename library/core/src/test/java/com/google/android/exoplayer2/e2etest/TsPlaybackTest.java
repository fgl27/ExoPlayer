/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.e2etest;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import androidx.test.core.app.ApplicationProvider;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.robolectric.PlaybackOutput;
import com.google.android.exoplayer2.robolectric.ShadowMediaCodecConfig;
import com.google.android.exoplayer2.robolectric.TestPlayerRunHelper;
import com.google.android.exoplayer2.testutil.AutoAdvancingFakeClock;
import com.google.android.exoplayer2.testutil.DumpFileAsserts;
import com.google.common.collect.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;
import org.robolectric.annotation.Config;

/** End-to-end tests using TS samples. */
// TODO(b/143232359): Remove once https://issuetracker.google.com/143232359 is resolved.
@Config(sdk = 29)
@RunWith(ParameterizedRobolectricTestRunner.class)
public class TsPlaybackTest {

  // TODO: Add samples with >2 audio channels when supported (sample.ac3, sample_ac3.ts,
  //       sample.eac3, sample_eac3joc.ec3, sample_eac3joc.ts, sample_eac3.ts).
  @Parameters(name = "{0}")
  public static ImmutableList<String> mediaSamples() {
    return ImmutableList.of(
        "bbb_2500ms.ts",
        "elephants_dream.mpg",
        "sample.ac4",
        "sample_ac4.ts",
        "sample.adts",
        "sample_ait.ts",
        "sample_cbs_truncated.adts",
        "sample_h262_mpeg_audio.ps",
        "sample_h262_mpeg_audio.ts",
        "sample_h263.ts",
        "sample_h264_dts_audio.ts",
        "sample_h264_mpeg_audio.ts",
        "sample_h264_no_access_unit_delimiters.ts",
        "sample_h265.ts",
        "sample_latm.ts",
        "sample_scte35.ts",
        "sample_with_id3.adts",
        "sample_with_junk");
  }

  @Parameter public String inputFile;

  @Rule
  public ShadowMediaCodecConfig mediaCodecConfig =
      ShadowMediaCodecConfig.forAllSupportedMimeTypes();

  @Test
  public void test() throws Exception {
    SimpleExoPlayer player =
        new SimpleExoPlayer.Builder(ApplicationProvider.getApplicationContext())
            .setClock(new AutoAdvancingFakeClock())
            .build();
    player.setVideoSurface(new Surface(new SurfaceTexture(/* texName= */ 1)));
    PlaybackOutput playbackOutput = PlaybackOutput.register(player, mediaCodecConfig);

    player.setMediaItem(MediaItem.fromUri("asset:///media/ts/" + inputFile));
    player.prepare();
    player.play();
    TestPlayerRunHelper.runUntilPlaybackState(player, Player.STATE_ENDED);
    player.release();

    DumpFileAsserts.assertOutput(
        ApplicationProvider.getApplicationContext(),
        playbackOutput,
        "playbackdumps/ts/" + inputFile + ".dump");
  }
}
