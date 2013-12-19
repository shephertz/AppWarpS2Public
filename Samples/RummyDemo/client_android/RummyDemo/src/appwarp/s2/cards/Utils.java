package appwarp.s2.cards;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	public static String userName = "";

	public static void showToastAlert(Context ctx, String message) {
		Toast.makeText(ctx.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	public static void showToastAlertOnUIThread(final Activity ctx,
			final String message) {
		ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(ctx.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static float getPercentFromValue(float number, float amount) {
		float percent = (number / amount) * 100;
		return percent;
	}

	public static float getValueFromPercent(float percent, float amount) {
		float value = (percent / 100) * amount;
		return value;
	}

	public static float getRatioValue(float a, float b, float x, float y) {
		if (x == 0) {
			return (a * y) / b;
		} else if (y == 0) {
			return (b * x) / a;
		}
		return 0;
	}

	public static Bitmap[] getCardsBitmapArray(Context context, int cardBlockWidth) {
		Bitmap cardSprite = getBitmapFromAsset(context, "carddeck_tiled.png");
		Log.d("getBitmapFromAsset", "BitmapWidth " + cardSprite.getWidth()+"x"+cardSprite.getHeight());
		Bitmap[] cards = null;
		int stratX = 0;
		int startY = 0;
		int cardWidth = cardSprite.getWidth() / 13;
		int cardHeight = cardSprite.getHeight() / 4;
		int ratioCardHeight = (int)Utils.getRatioValue(cardWidth, cardHeight, cardBlockWidth, 0);
		Log.d("ratioCardHeight", ratioCardHeight+"");
		cards = new Bitmap[52];
		for (int i = 1; i <= cards.length; i++) {
			cards[i - 1] = Bitmap.createBitmap(cardSprite, stratX, startY,
					cardWidth, cardHeight);
			cards[i - 1] = Bitmap.createScaledBitmap(cards[i - 1], cardBlockWidth, ratioCardHeight, false);
			stratX += cardWidth;
			if (i % 13 == 0) {
				stratX = 0;
				startY += cardHeight;
			}
		}
		return cards;
	}

	public static Bitmap getBitmapFromAsset(Context context, String strName) {
		AssetManager assetManager = context.getAssets();

		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(strName);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			return null;
		}

		return bitmap;
	}

}
