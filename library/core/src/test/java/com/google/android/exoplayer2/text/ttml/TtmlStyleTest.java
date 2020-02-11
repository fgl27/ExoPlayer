/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.google.android.exoplayer2.text.ttml;

import static android.graphics.Color.BLACK;
import static com.google.android.exoplayer2.text.ttml.TtmlStyle.STYLE_BOLD;
import static com.google.android.exoplayer2.text.ttml.TtmlStyle.STYLE_BOLD_ITALIC;
import static com.google.android.exoplayer2.text.ttml.TtmlStyle.STYLE_ITALIC;
import static com.google.android.exoplayer2.text.ttml.TtmlStyle.STYLE_NORMAL;
import static com.google.android.exoplayer2.text.ttml.TtmlStyle.UNSPECIFIED;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import android.graphics.Color;
import android.text.Layout;
import androidx.annotation.ColorInt;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.exoplayer2.text.Cue;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link TtmlStyle}. */
@RunWith(AndroidJUnit4.class)
public final class TtmlStyleTest {

  private static final String ID = "id";
  private static final String FONT_FAMILY = "serif";
  @ColorInt private static final int FONT_COLOR = Color.WHITE;
  private static final float FONT_SIZE = 12.5f;
  @TtmlStyle.FontSizeUnit private static final int FONT_SIZE_UNIT = TtmlStyle.FONT_SIZE_UNIT_EM;
  @ColorInt private static final int BACKGROUND_COLOR = Color.BLACK;
  private static final Layout.Alignment TEXT_ALIGN = Layout.Alignment.ALIGN_CENTER;
  private static final boolean TEXT_COMBINE = true;
  @Cue.VerticalType private static final int VERTICAL_TYPE = Cue.VERTICAL_TYPE_RL;

  private final TtmlStyle populatedStyle =
      new TtmlStyle()
          .setId(ID)
          .setItalic(true)
          .setBold(true)
          .setBackgroundColor(BACKGROUND_COLOR)
          .setFontColor(FONT_COLOR)
          .setLinethrough(true)
          .setUnderline(true)
          .setFontFamily(FONT_FAMILY)
          .setFontSize(FONT_SIZE)
          .setFontSizeUnit(FONT_SIZE_UNIT)
          .setTextAlign(TEXT_ALIGN)
          .setTextCombine(TEXT_COMBINE)
          .setVerticalType(VERTICAL_TYPE);

  @Test
  public void testInheritStyle() {
    TtmlStyle style = new TtmlStyle();
    style.inherit(populatedStyle);

    assertWithMessage("id must not be inherited").that(style.getId()).isNull();
    assertThat(style.isUnderline()).isTrue();
    assertThat(style.isLinethrough()).isTrue();
    assertThat(style.getStyle()).isEqualTo(STYLE_BOLD_ITALIC);
    assertThat(style.getFontFamily()).isEqualTo(FONT_FAMILY);
    assertThat(style.getFontColor()).isEqualTo(FONT_COLOR);
    assertThat(style.getFontSize()).isEqualTo(FONT_SIZE);
    assertThat(style.getFontSizeUnit()).isEqualTo(FONT_SIZE_UNIT);
    assertThat(style.getTextAlign()).isEqualTo(TEXT_ALIGN);
    assertThat(style.getTextCombine()).isEqualTo(TEXT_COMBINE);
    assertWithMessage("backgroundColor should not be inherited")
        .that(style.hasBackgroundColor())
        .isFalse();
    assertWithMessage("verticalType should not be inherited")
        .that(style.getVerticalType())
        .isEqualTo(Cue.TYPE_UNSET);
  }

  @Test
  public void testChainStyle() {
    TtmlStyle style = new TtmlStyle();

    style.chain(populatedStyle);

    assertWithMessage("id must not be inherited").that(style.getId()).isNull();
    assertThat(style.isUnderline()).isTrue();
    assertThat(style.isLinethrough()).isTrue();
    assertThat(style.getStyle()).isEqualTo(STYLE_BOLD_ITALIC);
    assertThat(style.getFontFamily()).isEqualTo(FONT_FAMILY);
    assertThat(style.getFontColor()).isEqualTo(FONT_COLOR);
    assertThat(style.getFontSize()).isEqualTo(FONT_SIZE);
    assertThat(style.getFontSizeUnit()).isEqualTo(FONT_SIZE_UNIT);
    assertThat(style.getTextAlign()).isEqualTo(TEXT_ALIGN);
    assertThat(style.getTextCombine()).isEqualTo(TEXT_COMBINE);
    assertWithMessage("backgroundColor should be chained")
        .that(style.getBackgroundColor())
        .isEqualTo(BACKGROUND_COLOR);
    assertWithMessage("verticalType should be chained")
        .that(style.getVerticalType())
        .isEqualTo(VERTICAL_TYPE);
  }

  @Test
  public void testStyle() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.getStyle()).isEqualTo(UNSPECIFIED);
    style.setItalic(true);
    assertThat(style.getStyle()).isEqualTo(STYLE_ITALIC);
    style.setBold(true);
    assertThat(style.getStyle()).isEqualTo(STYLE_BOLD_ITALIC);
    style.setItalic(false);
    assertThat(style.getStyle()).isEqualTo(STYLE_BOLD);
    style.setBold(false);
    assertThat(style.getStyle()).isEqualTo(STYLE_NORMAL);
  }

  @Test
  public void testLinethrough() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.isLinethrough()).isFalse();
    style.setLinethrough(true);
    assertThat(style.isLinethrough()).isTrue();
    style.setLinethrough(false);
    assertThat(style.isLinethrough()).isFalse();
  }

  @Test
  public void testUnderline() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.isUnderline()).isFalse();
    style.setUnderline(true);
    assertThat(style.isUnderline()).isTrue();
    style.setUnderline(false);
    assertThat(style.isUnderline()).isFalse();
  }

  @Test
  public void testFontFamily() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.getFontFamily()).isNull();
    style.setFontFamily(FONT_FAMILY);
    assertThat(style.getFontFamily()).isEqualTo(FONT_FAMILY);
    style.setFontFamily(null);
    assertThat(style.getFontFamily()).isNull();
  }

  @Test
  public void testFontColor() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.hasFontColor()).isFalse();
    style.setFontColor(Color.BLACK);
    assertThat(style.hasFontColor()).isTrue();
    assertThat(style.getFontColor()).isEqualTo(BLACK);
  }

  @Test
  public void testFontSize() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.getFontSize()).isEqualTo(0);
    style.setFontSize(10.5f);
    assertThat(style.getFontSize()).isEqualTo(10.5f);
  }

  @Test
  public void testFontSizeUnit() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.getFontSizeUnit()).isEqualTo(UNSPECIFIED);
    style.setFontSizeUnit(TtmlStyle.FONT_SIZE_UNIT_EM);
    assertThat(style.getFontSizeUnit()).isEqualTo(TtmlStyle.FONT_SIZE_UNIT_EM);
  }

  @Test
  public void testBackgroundColor() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.hasBackgroundColor()).isFalse();
    style.setBackgroundColor(Color.BLACK);
    assertThat(style.hasBackgroundColor()).isTrue();
    assertThat(style.getBackgroundColor()).isEqualTo(BLACK);
  }

  @Test
  public void testId() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.getId()).isNull();
    style.setId(ID);
    assertThat(style.getId()).isEqualTo(ID);
    style.setId(null);
    assertThat(style.getId()).isNull();
  }

  @Test
  public void testTextAlign() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.getTextAlign()).isNull();
    style.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
    assertThat(style.getTextAlign()).isEqualTo(Layout.Alignment.ALIGN_OPPOSITE);
    style.setTextAlign(null);
    assertThat(style.getTextAlign()).isNull();
  }

  @Test
  public void testTextCombine() {
    TtmlStyle style = new TtmlStyle();

    assertThat(style.getTextCombine()).isFalse();
    style.setTextCombine(true);
    assertThat(style.getTextCombine()).isTrue();
  }
}
