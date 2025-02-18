 783* podman exec -it recursing_sutherland /bin/bash
  784  podman rm -f recursing_sutherland
  785  git pull
  786  podman build -t python-app .
  787  podman run -dit -p 5000:5000 localhost/python-app:latest -n app01
  788  podman run -dit -p 5000:5000 localhost/python-app:latest
  789  podman ps -a
  790  podman run -dit -p 5000:5000 localhost/python-app:latest | grep -i python
  791  podman ps -a
  792  podman rm -f 477e99619fd0
  793  podman ps -a
  794  podman rm -f reverent_roentgen
  795  podman ps -a
  796  history
[root@sgctcrhclient02 python-app]# podman ps -a
CONTAINER ID  IMAGE                               COMMAND               CREATED        STATUS                                                   NAMES
5b90644517a6  docker.io/library/haproxy           haproxy -f /usr/l...  23 months ago  Exited tcp, 0.0.0.0:443->443/tcp                         haproxy
c3a81d8e5f4e  docker.io/gitlab/gitlab-ce:latest   /assets/wrapper       3 weeks ago    Up 5 datcp, 0.0.0.0:443->443/tcp, 0.0.0.0:55466->22/tcp  gitlab
1ed1f960bf6c  docker.io/library/sonarqube:latest                        2 weeks ago    Up 5 da000/tcp                                           sonarqube
39bde574bac5  localhost/python-app:latest         python app.py         3 minutes ago  Up 3 mi000/tcp                                           friendly_goldberg
[root@sgctcrhclient02 python-app]#
