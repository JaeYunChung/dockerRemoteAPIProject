package org.example;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main
{
    private static final String DOCKER_IP="[host ip주소:port]";
    public static void main(String[] args) throws DockerException, InterruptedException{
        DockerClient dc = new DefaultDockerClient(DOCKER_IP);

        List<PortBinding> hostPort = new ArrayList<>();
        hostPort.add(PortBinding.of("0.0.0.0", "10080")); // 호스트 주소는 0.0.0.0:80

        Map<String, List<PortBinding>> portBindings = new HashMap<>(); // map = (컨테이너 포트, 호스트 주소&포트)
        portBindings.put("80/tcp", hostPort);

        final HostConfig hostConfig = HostConfig.builder() // 컨테이너의 tcp/80과 연경
                .portBindings(portBindings)
                .build();

        // 컨테이너 생성
        final ContainerCreation container = dc.createContainer(ContainerConfig.builder()
                .image("nginx")      // 이미지는 nginx 사용
                .hostConfig(hostConfig) // 호스트와 컨테이너 연결
                .attachStderr(false).attachStdin(false).attachStdout(false) //detach 옵션
                .build(), "mynginx"); // 컨테이너 이름 설정

        final String id = container.id();

        dc.startContainer(id);

    }
}
