package com.hys.videoService;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;

/**
 * @Auth 86191
 * @Date 2020/4/19
 */
public class TestGetVideoUrlFromAliyun {
    public static void main(String[] args) throws Exception {

        // 创建初始化对象
        DefaultAcsClient client = InitAliyunVideo.initVodClient("LTAI4FsjFUENH4wsNC8Ekgib", "oaiKUWKNuWvAWVMEiaGBTiOgygZea8");

        // 创建获取视频地址request和response
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        GetPlayInfoRequest request = new GetPlayInfoRequest();

        // 向request中设置视频的id
        request.setVideoId("dc0455ce4026406d92414d26856db7bb");

        // 调用初始化对象里面的方法，传递request，获取数据
        response= client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();

        for (GetPlayInfoResponse.PlayInfo playInfo:playInfoList){

            System.out.println("PlayInfo.PlayURL = " + playInfo.getPlayURL() );

        }

        System.out.println("VideoBase.Title = " + response.getVideoBase().getTitle() );

        System.out.println("RequestId = " + response.getRequestId() );


    }

    /*获取播放地址函数*/
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("视频ID");
        return client.getAcsResponse(request);
    }
}
