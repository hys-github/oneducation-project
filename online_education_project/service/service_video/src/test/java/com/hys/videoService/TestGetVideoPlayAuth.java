package com.hys.videoService;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

/**
 * @Auth 86191
 * @Date 2020/4/19
 */
public class TestGetVideoPlayAuth {
    public static void main(String[] args) throws Exception {

        DefaultAcsClient client = InitAliyunVideo.initVodClient("LTAI4FsjFUENH4wsNC8Ekgib", "oaiKUWKNuWvAWVMEiaGBTiOgygZea8");

        // 得到视频的凭证
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        // 设置上传上去的每一个视频独有的id
        request.setVideoId("dc0455ce4026406d92414d26856db7bb");

        response = client.getAcsResponse(request);

        // 得到播放凭证
        String playAuth = response.getPlayAuth();
        System.out.println("playAuth===="+playAuth);

        //VideoMeta信息
        System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");

    }
}
