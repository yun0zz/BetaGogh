package com.example.homeactivity.Filter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.BitmapCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.homeactivity.Home.HomeActivity;
import com.example.homeactivity.Profile.AccountSettingsActivity;
import com.example.homeactivity.R;
import com.example.homeactivity.Share.NextActivity;
import com.example.homeactivity.Share.ShareActivity;
import com.example.homeactivity.Utils.BitmapUtils;
import com.example.homeactivity.Utils.CommentListAdapter;
import com.example.homeactivity.Utils.FirebaseMethods;
import com.example.homeactivity.Utils.HttpClient;
import com.example.homeactivity.Utils.UniversalImageLoader;
import com.example.homeactivity.models.Param;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.PendingIntent.getActivity;

public class FilterActivity extends AppCompatActivity {

    private static final String TAG = FilterActivity.class.getSimpleName();

    public static final String IMAGE_NAME = "image.png";

    public static final int SELECT_GALLERY_IMAGE = 101;

    private LinearLayout mFilterLayout;
    private RecyclerView mFilterListView;
    private FilterAdapter mAdapter;

    // Server URL
    public static final String Base_Url = "http://34.97.133.234:8000/";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    //vars
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage;
    private int imageCount = 0;
    private String imgUrl;
    private Bitmap bitmap;
    private Intent intent;

    public static String OriginalUrl;
    public static String final_image;

    //widgets
    private ImageView mBackArrow;
    private Context mContext = FilterActivity.this;

    //progressbar
    private ProgressBar mProgressBar;

    /**
     * filter style
     */
    private final FilterType[] types = new FilterType[]{
            FilterType.NONE,
            FilterType.STYLE1,
            FilterType.STYLE2,
            FilterType.STYLE3,
            FilterType.STYLE4,
            FilterType.STYLE5,
            FilterType.STYLE6,
            FilterType.STYLE7,
            FilterType.STYLE8,
            FilterType.STYLE9,
            FilterType.STYLE10,
            FilterType.STYLE11,
            FilterType.STYLE12,
            FilterType.STYLE13,
            FilterType.STYLE14
    };
    /***/


    @BindView(R.id.image_preview)
    ImageView imagePreview;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    ImageView imageView;
    String encoded = "";


    Bitmap originalImage;
    // to backup image with filter applied

    // the final image after applying
    // brightness, saturation, contrast
    Bitmap finalImage;

    // load native image filters library
    static {
        System.loadLibrary("NativeImageProcessor");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter_activity_main);

        /** next 버튼 */
        TextView next = findViewById(R.id.filter_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NextActivity.class);

                /** 출력 이미지 정보 NextActivity로 보내기 */
                //최종 url
                String final_image = Base_Url + "transfer/" + mAdapter.Filterid() + "/" + OriginalUrl;
                intent.putExtra("selected_image", final_image);

                startActivity(intent);
            }
        });

        /** 뒤로가기 */
        ImageView backArrow = (ImageView) findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: back activity.");
                finish();
            }
        });

        /** 미리보기 사진 */

        imageView = findViewById(R.id.image_preview);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        this.compressImage(bitmap);

        new CallApi(this.encoded, imageView, "filter-21-th-hd").execute();

        ButterKnife.bind(this);

        loadImage();

        sendRegistReqWithRetrofit(0);

        initView();

        Log.d(TAG, "5678");
    }

    private void initView() {

        mFilterLayout = (LinearLayout) findViewById(R.id.layout_filter);
        mFilterListView = (RecyclerView) findViewById(R.id.recycler_view);

        /** filter */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);

        mAdapter = new FilterAdapter(this, types);
        mFilterListView.setAdapter(mAdapter);

        mAdapter.setOnFilterChangeListener(onFilterChangeListener);
    }

    private FilterAdapter.onFilterChangeListener onFilterChangeListener = new FilterAdapter.onFilterChangeListener() {

        @Override
        public void onFilterChanged(FilterType filterType) {
            mAdapter.Filterid();
            sendRegistReqWithRetrofit(mAdapter.Filterid());
        }
    };

    // load the default image from assets on app launch
    private void loadImage() {
        originalImage = BitmapUtils.getBitmapFromAssets(this, IMAGE_NAME, 300, 300);
        imagePreview.setImageBitmap(originalImage);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /*************************/
    private void openImageFromGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, SELECT_GALLERY_IMAGE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /*
     * saves image to camera gallery
     * */
    private void saveImageToGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            final String path = BitmapUtils.insertImage(getContentResolver(), finalImage, System.currentTimeMillis() + "_profile.jpg", null);
                            if (!TextUtils.isEmpty(path)) {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Image saved to gallery!", Snackbar.LENGTH_LONG)
                                        .setAction("OPEN", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                openImage(path);
                                            }
                                        });

                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Unable to save image!", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    // opening image in default image viewer app
    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "image/*");
        startActivity(intent);
    }

    private class CallApi extends AsyncTask<Object, Object, String> {
        String encoded;
        String file_link;
        ImageView imageView;
        String type;

        public CallApi(String encoded, ImageView imageView, String type) {
            this.encoded = encoded;
            this.imageView = imageView;
            this.type = type;
        }

        @Override
        protected String doInBackground(Object... objects) {
            ArrayList<Param> params = new ArrayList<>();
            params.add(new Param("fileToUpload", this.encoded));
            try {
                this.file_link = new HttpClient().makeHttpRequestPost("http://color.photofuneditor.com/" + this.type, params).optString("file_link");
                return this.file_link;
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Log.d("Result", res);

            //Picasso.with(FilterActivity.this).load("http://color.photofuneditor.com/output/" + res).into(imageView);
            Picasso.get().load("http://color.photofuneditor.com/output/" + res).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Bitmap newBitmap = bitmap.copy(bitmap.getConfig(), true);
                    Bitmap n1 = Bitmap.createBitmap(newBitmap, 0, 20, newBitmap.getWidth(), newBitmap.getHeight() - 30);
                    imageView.destroyDrawingCache();
                    imageView.setImageBitmap(n1);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        }
    }


    private void compressImage(Bitmap imageViewBitmap) {
        this.bitmap = imageViewBitmap;
        if (BitmapCompat.getAllocationByteCount(this.bitmap) < 512000) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            scaleBitmapAndKeepRation(this.bitmap, 1024, 1024).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            this.encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
            return;
        }
        int division = 25600000 / BitmapCompat.getAllocationByteCount(this.bitmap);
        if (division == 0) {
            division = 1;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaleBitmapAndKeepRation(this.bitmap, 512, 512).compress(Bitmap.CompressFormat.PNG, division, byteArrayOutputStream);
        this.encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    public static Bitmap scaleBitmapAndKeepRation(Bitmap TargetBmp, int reqHeightInPixels, int reqWidthInPixels) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0.0f, 0.0f, (float) TargetBmp.getWidth(), (float) TargetBmp.getHeight()), new RectF(0.0f, 0.0f, (float) reqWidthInPixels, (float) reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(TargetBmp, 0, 0, TargetBmp.getWidth(), TargetBmp.getHeight(), m, true);

    }


    /*** 메뉴 Save, Open **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_open) {
            openImageFromGallery();
            return true;
        }

        if (id == R.id.action_save) {
            saveImageToGallery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 서버에서 결과 이미지 가져오기
     */
    public void sendRegistReqWithRetrofit(final int filter_id) {
            if (filter_id == 0) {
                Glide.with(FilterActivity.this).load(OriginalUrl).centerCrop().into(imagePreview);
            } else {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.MINUTES)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .writeTimeout(5, TimeUnit.MINUTES)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Base_Url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServiceApi serviceApi = retrofit.create(ServiceApi.class);
                serviceApi.SendInfo(filter_id, OriginalUrl); // 선택한 filter_id와 url을 서버에 전송, 서버에서 결과 이미지 가져오기

                Log.d(TAG, "선택한스타일" + filter_id);

                /** imageview에 결과이미지 넣기 */
                String result_url = Base_Url + "preview/" + filter_id + "/" + OriginalUrl;

                Toast.makeText(getApplicationContext(), "인공지능이 그림을 그리고 있어요", Toast.LENGTH_LONG).show();
                Glide.with(FilterActivity.this).load(result_url).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imagePreview);


            }

    }

    //Get OriginalUrl
    public static void setOriginalPhoto(String firebaseUrl){
        OriginalUrl = firebaseUrl;
        Log.d(TAG, "onSuccess 갤러리 오리지널1 Url: " + OriginalUrl);
    }
}