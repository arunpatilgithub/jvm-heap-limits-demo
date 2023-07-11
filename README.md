# jvm-heap-limits-demo

Demo project to view JVM heap allocation based on the resource constraint put using ARGS, docker entrypoint args and kubernetes pod resource limits.


Part 1: Run application on local machine outside docker.

On my system with 16GB ram and 10 core cpi, below is the output from http://localhost:8080/memory-usage

`{
"xmsInMB": 256,
"xmxInMB": 4096
}`

Thus, the max allocated heap size is 1/4th of the machine ram.

Part 2: Run application as a docker container.

Note: I have allocated 6GB ram and 5 cpus for docker host. 

First build a local docker image using command (including the dot towards the end in the command)

`docker build -t jvm-heap-limits-demo .`

Then run the image with command as below

`docker run -d --name jvm-heap-limits-demo-app -p 8080:8080 jvm-heap-limits-demo`

Now th memory usage looks like below.

`{
"xmsInMB": 94,
"xmxInMB": 1486
}`

Note: We can remove the container with command
`docker rm -f jvm-heap-limits-demo-app`