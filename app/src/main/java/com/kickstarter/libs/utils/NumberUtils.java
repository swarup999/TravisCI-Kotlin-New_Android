package com.kickstarter.libs.utils;

import com.kickstarter.libs.NumberOptions;
import com.kickstarter.libs.utils.extensions.AnyExtKt;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import androidx.annotation.NonNull;

public final class NumberUtils {
  private NumberUtils() {}

  public static @NonNull String flooredPercentage(final float value) {
    return flooredPercentage(value, Locale.getDefault());
  }

  public static @NonNull String flooredPercentage(final float value, final @NonNull Locale locale) {
    final NumberFormat numberFormat = NumberFormat.getPercentInstance(locale);
    numberFormat.setRoundingMode(RoundingMode.DOWN);
    return numberFormat.format(value / 100);
  }

  /**
   * Returns a formatted number for the user's locale.
   */
  public static @NonNull String format(final int value) {
    return format(value, Locale.getDefault());
  }

  /**
   * Returns a formatted number for the specified locale.
   */
  public static @NonNull String format(final int value, final @NonNull Locale locale) {
    return NumberFormat.getIntegerInstance(locale).format(value);
  }

  /**
   * Returns a formatted number for the user's locale. Defaults to 0 precision with no bucketing.
   */
  public static @NonNull String format(final float value) {
    return format(value, NumberOptions.builder().build());
  }

  /**
   * Returns a formatted number for the user's locale. {@link NumberOptions} can control whether the number is
   * used as a currency, if it is bucketed, and the precision.
   */
  public static @NonNull String format(final float value, final @NonNull NumberOptions options) {
    return format(value, options, Locale.getDefault());
  }

  /**
   * Returns a formatted number for a given locale. {@link NumberOptions} can control whether the number is
   * used as a currency, if it is bucketed, and the precision.
   */
  public static @NonNull String format(final float value, final @NonNull NumberOptions options,
    final @NonNull Locale locale) {

    final NumberFormat numberFormat = numberFormat(options, locale);
    if (numberFormat instanceof DecimalFormat) {
      numberFormat.setRoundingMode(AnyExtKt.coalesce(options.roundingMode(), RoundingMode.HALF_DOWN));
    }

    int precision = AnyExtKt.coalesce(options.precision(), 0);
    float divisor = 1.0f;
    String suffix = "";

    // TODO: The bucketing logic works, but the suffix should be translated.
    final float bucketAbove = AnyExtKt.coalesce(options.bucketAbove(), 0.0f);

    if (bucketAbove >= 1000.0f && value >= bucketAbove) {
      if (bucketAbove > 0.0f && bucketAbove < 1_000_000.0f) {
        divisor = 1000.0f;
        suffix = "K";
      } else if (bucketAbove >= 1_000_000.0f) {
        divisor = 1_000_000.0f;
        suffix = "M";
      }
      if (options.bucketAbove() != null) {
        precision = AnyExtKt.coalesce(options.bucketPrecision(), 0);
      }
    }

    if (options.currencyCode() != null) {
      suffix = String.format("%s %s", suffix, options.currencyCode());
    }

    numberFormat.setMinimumFractionDigits(precision);
    numberFormat.setMaximumFractionDigits(precision);

    float bucketedValue = value;
    if (value >= bucketAbove) {
      bucketedValue = value / divisor;
    }

    return String.format("%s%s", numberFormat.format(bucketedValue), suffix).trim();
  }

  /**
   * Returns a boolean that determines if a Double is a whole number.
   *
   * @param value a Double to be verified if it is a whole number.
   */
  private static Boolean isWholeNumber(final @NonNull Double value) {
    return value == Math.round(value);
  }

  /**
   * @deprecated Use com.kickstarter.libs.utils.extensions.StringExt#parseToDouble(java.lang.String)
   */
  @Deprecated
  public static double parse(final @NonNull String input) {
    return parse(input, Locale.getDefault());
  }

  /**
   * @deprecated Use instead com.kickstarter.libs.utils.extensions.StringExt#parseToDouble(java.lang.String)
   */
  @Deprecated
  public static double parse(final @NonNull String input, final @NonNull Locale locale) {
    try {
      return NumberFormat.getNumberInstance(locale).parse(input).doubleValue();
    } catch (ParseException e) {
      return 0.0;
    }
  }

  /**
   * Returns a precision based on the RoundingMode.
   *
   * @param amount When this is a whole number, we return 0, otherwise we return 2.
   * @param roundingMode When this is not HALF_UP, we return 0, otherwise we return 2.
   */
  public static int precision(final @NonNull Double amount, final @NonNull RoundingMode roundingMode) {
    if (roundingMode != RoundingMode.HALF_UP || isWholeNumber(amount)) {
      return 0;
    } else {
      return 2;
    }
  }

  /**
   * Return a formatter that can output an appropriate number based on the input currency and locale.
   */
  private static @NonNull NumberFormat numberFormat(final @NonNull NumberOptions options, final @NonNull Locale locale) {
    final NumberFormat numberFormat;

    if (options.isCurrency()) {
      final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
      final DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
      symbols.setCurrencySymbol(options.currencySymbol());
      decimalFormat.setDecimalFormatSymbols(symbols);
      numberFormat = decimalFormat;
    } else {
      numberFormat = NumberFormat.getInstance(locale);
    }

    return numberFormat;
  }
}
