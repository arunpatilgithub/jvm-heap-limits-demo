# jvm-heap-limits-demo

Demo project to view JVM heap allocation based on the resource constraint put using ARGS, docker entrypoint args and kubernetes pod resource limits.

Note: I am updating the version of the memory-usage api from v1, v2 just to make sure I pick updated code
after building the jar, and docker image. you can keep the same version.

Part 1: Run application on local machine outside docker.

On my system with 16GB ram and 10 core cpi, below is the output from http://localhost:8080/memory-usage

`{
"xmsInMB": 256,
"xmxInMB": 4096
}`

Thus, the max allocated heap size is 1/4th of the machine ram.

Part 2: Run application as a docker container(Dockerfile-v1).

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

Part 3: Run application in kubernetes cluster with resource limits in deployment spec(deployment-v1.yml).

Note: I am deploying springboot service image on local kubernetes cluster with help of "kind".
https://kind.sigs.k8s.io/docs/user/quick-start/ 

We can create docker image as mentioned in Part 2(rename Dockerfile-v1) and load that image in kind cluster with help of
command `kind load docker-image jvm-heap-limits-demo-v1`
Here I have created a docker image with name jvm-heap-limits-demo-v1

Once image is loaded on kind cluster, we can apply the deployment.yml in this project folder to create
pods for our springboot service image. Note that we have specified resource limits(500Mi) for this service
in deployment.yaml.

To test the service url, we need to use port-forwarding for our deployed pod
so that we can hit the pod url from our local machine.
More info on port-forwarding is here -> https://kubernetes.io/docs/tasks/access-application-cluster/port-forward-access-application-cluster/

Below is the command I used for port-forwarding

`kubectl port-forward deploy/jvm-heap-limits-demo-app-v1 9001:9002`

Now try hitting the url http://localhost:9001/v1/memory-usage

You should see an output similar to below.

````
{
    "xmsInMB": 8,
    "xmxInMB": 121
}
````

JVM allocated here too is approximately 1\4th of the memory limit allocated to the pod.

Part 3: Setting Xmx in Dockerfile(v2) and also resource-limits in deployment.yaml to verify which
one overrides.
Here we have set Xmx to 1G which means, we are explicitly setting the max heap size and not replying
on JVM to pick for us. And we have specified memory limit to 500Mi in deployment-v2.yml.


Performing all the tasks like building the jar, docker image
and loading it in kind and finally applying the deployment.yml template.

Now try hitting the url http://localhost:9001/v2/memory-usage
You should see an output similar to below.

```
{
    "xmsInMB": 8,
    "xmxInMB": 989
}
```

This the Xmx in Dockerfile takes precedence.