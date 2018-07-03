package com.lcsd.dongzhi.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.http.AppConfig;
import com.lcsd.dongzhi.http.AppContext;
import com.lcsd.dongzhi.util.L;
import com.lcsd.dongzhi.dialog.Select_dialog;
import com.lcsd.dongzhi.util.StringUtil;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class PaikeUploadActivity extends BaseBindActivity implements View.OnClickListener {
    @BindView(R.id.tv_qx)
    TextView tv_qx;
    @BindView(R.id.tv_fb)
    TextView tv_fb;
    @BindView(R.id.et_paike)
    EditText ed;
    @BindView(R.id.gv_selected_img)
    GridView gv;
    @BindView(R.id.ll_location)
    LinearLayout ll;
    @BindView(R.id.img_location)
    ImageView iv_location;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.video_selected)
    VideoView mVideo;
    @BindView(R.id.ll_video_selected)
    LinearLayout ll_video;
    @BindView(R.id.top_view)
    View mView;
    private Context mContext;

    private static final int CHECK_PERMISSION = 8001;
    //定位回調标识
    private static final int LoCATION = 1;
    //选择图片回调标识
    private static final int PICK_PHOTO = 2;
    //删除图片回调标识
    private static final int DELETE_PHOTO = 3;
    //选择本地视频回调标识
    private static final int CHOOSE_VIDEO = 4;
    //查看视频的回调标识
    private static final int WATCHVIDEO = 5;
    //拍摄视频
    private static final int CAMERA_RQ = 6;
    //本地视频播放地址
    private Uri uri;
    private String videourlpath;
    private List<Uri> mResults = new ArrayList<>();
    private GridAdapter adapter;
    //记录已选择的图片数
    private int mSelected = 0;
    //判断最后一个还能不能加图
    private boolean mIstrue = true;
    private boolean mIszhihu = false;
    String[] single_list = {"拍摄", "相册", "本地视频"};
    private ProgressDialog progressDialog;
    private Select_dialog dialog;
    private AlertView alertView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_paike_upload;
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
    }

    @Override
    protected void initView() {
        mContext = this;
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.3f)
                .init();
        //加入一个空数据
        mResults.add(null);
        adapter = new GridAdapter(this);
        gv.setAdapter(adapter);
        progressDialog = new ProgressDialog(mContext);
        dialog = new Select_dialog(mContext);
        initListener();
    }

    private void initListener() {
        ll.setOnClickListener(this);
        ll_video.setOnClickListener(this);
        tv_qx.setOnClickListener(this);
        tv_fb.setOnClickListener(this);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mResults.size() - 1 && mIstrue) {
                    if (mIszhihu && mSelected > 0) {
                        toZhihu();
                    } else {
                        showSelectDialog();
                    }
                } else {
                    //浏览选中图片
                    String[] imgs;
                    if (mSelected < 9) {
                        imgs = new String[mResults.size() - 1];
                        for (int i = 0; i < imgs.length; i++) {
                            imgs[i] = mResults.get(i).toString();
                        }
                    } else {
                        imgs = new String[mResults.size()];
                        for (int i = 0; i < imgs.length; i++) {
                            imgs[i] = mResults.get(i).toString();
                        }
                    }
                    Intent intent = new Intent(mContext, PaikeImagePageActivity.class).putExtra("index", position).putExtra("imgs", imgs);
                    startActivityForResult(intent, DELETE_PHOTO);
                }
            }
        });
    }

    //选择
    private void showSelectDialog() {
        alertView = new AlertView("上传内容", null, "取消", null,
                single_list,
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        startRecordVideo();
                    } else {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO}, CHECK_PERMISSION);
                    }
                } else if (position == 1) {
                    toZhihu();
                    mIszhihu = true;
                } else if (position == 2) {
                    SelectVideo();
                }
            }
        }).setCancelable(true);
        alertView.show();
    }

    //跳转拍摄
    public void startRecordVideo() {
        startActivityForResult(new Intent(mContext, CameraActivity.class), CAMERA_RQ);
    }

    //调用知乎图片选择
    private void toZhihu() {
        Matisse.from((Activity) mContext)
                .choose(MimeType.allOf()) // 选择 mime 的类型
                .countable(true) // 显示选择的数量
                .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
                .captureStrategy(new CaptureStrategy(true, "com.lcsd.dongzhi.MyFileProvider")) // 拍照的图片路径
                .theme(R.style.Matisse_Zhihu) // 蓝色背景
                .maxSelectable(9 - mSelected) // 图片选择的最多数量
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size)) // 列表中显示的图片大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(PICK_PHOTO); // 设置作为标记的请求码，返回图片时使用
    }

    /**
     * 选择本地视频
     */
    private void SelectVideo() {
        Intent video = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(video, CHOOSE_VIDEO);
    }

    /**
     * 播放视频
     *
     * @param uri
     */
    private void starVideo(final Uri uri) {
        mVideo.setVideoURI(uri);
        mVideo.start();
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideo.setVideoURI(uri);
                mVideo.start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_location:
                Intent intent = new Intent(mContext, PaikeLocationActivity.class);
                startActivityForResult(intent, LoCATION);
                break;
            case R.id.ll_video_selected:
                Intent videointent = new Intent(mContext, UploadvideoActivity.class);
                videointent.putExtra("videouri", uri.toString());
                startActivityForResult(videointent, WATCHVIDEO);
                break;
            case R.id.tv_qx:
                Signout();
                break;
            case R.id.tv_fb:
                if (ed.getText() != null && !StringUtil.isEmpty(ed.getText().toString())) {
                    request_upload();
                } else {
                    Toast.makeText(mContext, "请输入描述！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    //上传操作
    private void request_upload() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置顺序在前，不然不显示
        progressDialog.setTitle("正在上传");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Map<String, String> map = new HashMap<>();
        final Map<String, ArrayList<File>> map2 = new HashMap<>();
        final Map<String, File> map1 = new HashMap<>();
        final ArrayList<File> files = new ArrayList<>();
        final List<String> photos = new ArrayList<>();
        map.put("c", "usercp");
        map.put("f", "paike");
        map.put("title", ed.getText().toString());
        if (tv_location.getText() != null && (!tv_location.getText().toString().equals("所在位置"))) {
            map.put("address", tv_location.getText().toString());
        }
        if (mResults.size() > 1) {
            //获取真正选择的图片
            for (int i = 0; i < mResults.size(); i++) {
                if (mResults.get(i) != null) {
                    String path = StringUtil.getRealPathFromUri(mContext, mResults.get(i));
                    if (path != null) {
                        photos.add(path);
                        final File oldfile = new File(path);
                        L.d("原来大小路径：", oldfile.length() / 1024 + "kb 路径：" + oldfile.getPath());
                    } else {
                        String s = mResults.get(i).toString();
                        String ss = Environment.getExternalStorageDirectory().toString() + s.substring(s.indexOf("external_storage_root") + 21, s.length());
                        final File oldfile = new File(ss);
                        L.d("原来大小路径：", oldfile.length() / 1024 + "kb 路径：" + oldfile.getPath());
                        photos.add(ss);
                    }
                }
            }
            map2.put("picture[]", files);
        }
        if (uri != null || videourlpath != null) {
            if (videourlpath != null) {
                L.d("视频地址：", videourlpath);
                map1.put("video", new File(videourlpath));
            } else {
                String videopath = StringUtil.getRealPathFromUri(mContext, uri);
                if (videopath != null) {
                    L.d("视频地址：", videopath);
                    map1.put("video", new File(videopath));
                }
            }
            upload(map, map2, map1);
        } else if (photos != null && photos.size() > 0) {
            Luban.with(this)
                    .load(photos)
                    .ignoreBy(100)
                    .setTargetDir(getPath())
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            L.d("图片压缩：", "压缩开始");
                        }

                        @Override
                        public void onSuccess(File file) {
                            L.d("图片压缩：", "压缩成功,现在大小路径：" + file.length() / 1024 + "KB 路径 ：" + file.getPath());
                            files.add(file);
                            //全部压缩完成
                            if (files.size() == photos.size()) {
                                map2.put("picture[]", files);
                                upload(map, map2, map1);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            L.d("图片压缩：", "压缩失败：" + e.toString());
                            files.add(new File(photos.get(files.size())));
                            if (files.size() == photos.size()) {
                                map2.put("picture[]", files);
                                upload(map, map2, map1);
                            }
                        }
                    }).launch();
        } else {
            upload(map, map2, map1);
        }
    }

    private void upload(Map<String, String> map, Map<String, ArrayList<File>> map2, Map<String, File> map1) {
        AppContext.getInstance().getmMyOkHttp().upload(mContext, AppConfig.request_Data, map, map1, map2, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    L.d("上传拍客：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        progressDialog.dismiss();
                        Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                        if (object.getString("status").equals("ok")) {
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                L.d("上传错误：", error_msg);
                Toast.makeText(mContext, "上传出错，请稍后重试！", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                super.onProgress(currentBytes, totalBytes);
                progressDialog.setProgressNumberFormat(StringUtil.convertFileSize(currentBytes) + "/" + StringUtil.convertFileSize(totalBytes));
                progressDialog.setProgress((int) currentBytes);
                progressDialog.setMax((int) totalBytes);//做百分比更新
            }
        });
    }


    //根据回传值做相应操作
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //为intent赋值为data
        if (requestCode == LoCATION && data != null) {
            String str = data.getStringExtra("adree");
            if (str.length() > 0) {
                tv_location.setText(str);
                iv_location.setImageResource(R.drawable.img_location_selected);
            } else {
                tv_location.setText("所在位置");
                iv_location.setImageResource(R.drawable.img_location_selected_no);
            }
        } else if (requestCode == PICK_PHOTO && resultCode == RESULT_OK) {
            mSelected = mSelected + Matisse.obtainResult(data).size();
            addResult(Matisse.obtainResult(data));
        } else if (requestCode == DELETE_PHOTO && data != null) {
            String string = data.getStringExtra("type");
            if (string.equals("0")) {
                //全部删除
                mResults.clear();
                mResults.add(null);
                mSelected = 0;
                mIstrue = true;
                mIszhihu = false;
            } else if (string.equals("1")) {
                //删除部分
                mResults.clear();
                mResults.addAll(PaikeImagePageActivity.teamlist);
                mResults.add(null);
                mSelected = mResults.size() - 1;//减掉添加的空数据
                mIstrue = true;
            }
            adapter.notifyDataSetChanged();
        } else if (resultCode == Activity.RESULT_OK && requestCode == CHOOSE_VIDEO) {
            uri = data.getData();
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            try {
                mmr.setDataSource(mContext, uri);
                long duration = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)); // 播放时长单位为毫秒
                L.d("时间：", duration + "");
                if (duration > 120000) {
                    Toast.makeText(mContext, "请选择小于两分钟的视频！", Toast.LENGTH_SHORT).show();
                } else {
                    if (uri != null) {
                        gv.setVisibility(View.GONE);
                        ll_video.setVisibility(View.VISIBLE);
                        starVideo(uri);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mmr.release();
            }
        } else if (requestCode == WATCHVIDEO && data != null) {
            if (data.getIntExtra("delete", 0) == 1) {
                if (mVideo != null) {
                    mVideo.stopPlayback();
                }
                uri = null;
                ll_video.setVisibility(View.GONE);
                gv.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == 102 && data!=null) {
            try {
                uri = Uri.parse(data.getStringExtra("videouri"));
                videourlpath=data.getStringExtra("videouri");
                if (uri != null) {
                    gv.setVisibility(View.GONE);
                    ll_video.setVisibility(View.VISIBLE);
                    starVideo(uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == 103) {
            Toast.makeText(mContext, "请检查录音权限", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK && requestCode == 101) {

        }
    }

    //添加图片到gridview中
    private void addResult(List<Uri> list) {
        mResults.remove(mResults.size() - 1);
        if (mSelected < 9) {
            for (int i = 0; i < list.size(); i++) {
                mResults.add(list.get(i));
            }
            mResults.add(null);
        } else {
            mIstrue = false;
            for (int i = 0; i < list.size(); i++) {
                mResults.add(list.get(i));
            }
        }
        for (int i = 0; i < mResults.size() - 1; i++) {
            L.d("选择图片地址：", mResults.get(i).toString());
        }
        adapter.notifyDataSetChanged();
    }

    //退出编辑
    private void Signout() {
        dialog.setCancelable(false);
        dialog.show();
        dialog.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                PaikeUploadActivity.this.finish();
            }
        });
        dialog.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * gridview适配器
     */
    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return mResults.size();
        }

        public Object getItem(int arg0) {
            return mResults.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            // if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_sc, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            convertView.setTag(holder);
            /*} else {
                holder = (ViewHolder) convertView.getTag();
            }*/
            if (mResults.get(position) != null) {
                Glide.with(mContext).load(mResults.get(position)).crossFade().centerCrop().error(R.drawable.ic_default_color).into(holder.image);
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

    }

    //权限申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CHECK_PERMISSION
                && grantResults.length == 4
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
            startRecordVideo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideo != null) {
            mVideo.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideo != null) {
            mVideo.stopPlayback();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (alertView != null && alertView.isShowing()) {
            alertView.dismiss();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            Signout();
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }
}
