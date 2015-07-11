package com.sun.training.print_content;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.sun.training.R;

public class PrintContentActivity extends Activity implements OnClickListener {

	protected static final String TAG = PrintContentActivity.class
			.getSimpleName();
	private Button btn;
	final String txtPrintPhoto = "Print photo";
	final String txtPrintHtml = "Print HTML Document";

	private WebView mWebView;
	private List<PrintJob> mPrintJobs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);

		btn = (Button) findViewById(R.id.btn_common);
		btn.setText(txtPrintPhoto);
		btn.setOnClickListener(this);

		btn.setText(txtPrintHtml);
		mPrintJobs = new ArrayList<PrintJob>();
	}

	@Override
	public void onClick(View v) {
		if (v instanceof Button) {
			String txt = ((Button) v).getText().toString();
			if (txt.equals(txtPrintPhoto)) {
				doPhotoPrint();
			} else if (txt.equals(txtPrintHtml)) {
				doWebViewPrint();
			}
		}
	}

	private void doPhotoPrint() {
		// NOTE:
		// "java.lang.IllegalStateException: Can print only from an activity"
		// PrintHelper photoPrinter = new PrintHelper(getApplicationContext());
		PrintHelper photoPrinter = new PrintHelper(this);
		photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.img_zoro);
		photoPrinter.printBitmap("zoro.jpg - test print", bitmap);
	}

	private void doWebViewPrint() {
		WebView webView = new WebView(this);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				Log.i(TAG, "page finished loading " + url);
				createWebPrintJob(view);
				mWebView = null;
			}
		});

		String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, "
				+ "testing, testing...</p></body></html>";
		// webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8",
		// null);
		// --include graphics in the page
		// webView.loadDataWithBaseURL("file:///android_asset/images/",
		// htmlDocument, "text/HTML", "UTF-8", null);
		// --print an existing web page
		webView.loadUrl("http://www.baidu.com");

		// keep a reference to WebView object until you pass the
		// PrintDocumentAdapter to the PrintManager
		mWebView = webView;
	}

	@SuppressLint("NewApi")
	private void createWebPrintJob(WebView webView) {
		PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
		PrintDocumentAdapter printAdapter = webView
				.createPrintDocumentAdapter();
		String jobName = getString(R.string.app_name) + " Document";

		PrintJob printJob = printManager.print(jobName, printAdapter,
				new PrintAttributes.Builder().build());
		mPrintJobs.add(printJob);
	}

	// Print custom document
	@SuppressLint("NewApi")
	private void doPrint() {
		// get a PrintManager instance
		PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
		String jobName = getString(R.string.app_name) + " Document";

		printManager.print(jobName, new MyPrintDocumentAdapter(this), null);
	}

	@SuppressLint("NewApi")
	private class MyPrintDocumentAdapter extends PrintDocumentAdapter {
		private Activity activity;
		private PdfDocument mPdfDocument;

		int totalPages = 10;

		public MyPrintDocumentAdapter(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onLayout(PrintAttributes oldAttributes,
				PrintAttributes newAttributes,
				CancellationSignal cancellationSignal,
				LayoutResultCallback callback, Bundle extras) {
			mPdfDocument = new PrintedPdfDocument(activity, newAttributes);

			// response to cancellation request
			if (cancellationSignal.isCanceled()) {
				callback.onLayoutCancelled();
				return;
			}

			// compute the expected number of the printed pages
			int pages = computePageCount(newAttributes);
			if (pages > 0) {
				// return print information to print framework
				PrintDocumentInfo info = new PrintDocumentInfo.Builder(
						"print_output.pdf")
						.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
						.setPageCount(pages).build();
				callback.onLayoutFinished(info, true);
			} else {
				callback.onLayoutFailed("Page count calculation failed.");
			}
		}

		private int computePageCount(PrintAttributes printAttributes) {
			int itemsPerPage = 4;// default item count for portrait mode

			MediaSize pageSize = printAttributes.getMediaSize();
			if (!pageSize.isPortrait()) {// landscape mode
				itemsPerPage = 6;
			}

			int printItemCount = getPrintItemCount();

			return (int) Math.ceil(printItemCount / itemsPerPage);
		}

		private int getPrintItemCount() {
			return 10;
		}

		@Override
		public void onWrite(PageRange[] pages,
				ParcelFileDescriptor destination,
				CancellationSignal cancellationSignal,
				WriteResultCallback callback) {
			// Iterate over each page of the document, check if it's in the
			// output range.
			for (int i = 0; i < totalPages; i++) {
				if (containsPage(pages, i)) {
					// FIXME: unknown data type of writtenPagesArray
					// writtenPagesArray.append(writtenPagesArray.size(), i);
					PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(
							100, 60, i).create();
					PdfDocument.Page page = mPdfDocument.startPage(info);

					if (cancellationSignal.isCanceled()) {
						callback.onWriteCancelled();
						mPdfDocument.close();
						mPdfDocument = null;
						return;
					}

					drawPage(page);

					// rendering is complete, so page can be finalized.
					mPdfDocument.finishPage(page);
				}
			}

			// write PDF document to file
			try {
				mPdfDocument.writeTo(new FileOutputStream(destination
						.getFileDescriptor()));
			} catch (IOException e) {
				callback.onWriteFailed(e.toString());
				return;
			} finally {
				mPdfDocument.close();
				mPdfDocument = null;
			}
			PageRange[] writtenPages = computWrittenPages();
			// signal the print framework the document is complete
			callback.onWriteFinished(writtenPages);
		}

		private void drawPage(Page page) {
			Canvas canvas = page.getCanvas();

			// units are in points
			int titleBaseLine = 72;
			int leftMargin = 54;

			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setTextSize(36);
			canvas.drawText("Test title", leftMargin, titleBaseLine, paint);

			paint.setTextSize(11);
			canvas.drawText("test paragraph", leftMargin, titleBaseLine + 25,
					paint);

			paint.setColor(Color.BLUE);
			canvas.drawRect(100, 100, 172, 172, paint);
		}

		private PageRange[] computWrittenPages() {
			return null;
		}

		private boolean containsPage(PageRange[] pages, int i) {
			return true;
		}
	}

}
