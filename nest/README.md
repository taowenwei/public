## Run with Prometheus

1. config prometheus.yml

```yaml
  - job_name: "nest.js"
    metrics_path: "/swagger-stats/metrics"
    scrape_interval: 5s
    static_configs:
      - targets: ["localhost:3000"]
        labels:
          application: "nest"
```

2. run

```bash
./prometheus --config.file=prometheus.yml
```

## Run with Docker

1. create a envrc file for MongoDB access URL

```bash
DBURL=mongodb+srv://<USERNAME>:<PASSWORD>@<MongoDB URL>/<TABLE name>?retryWrites=true&w=majority
```

2. docker run
```bash
docker run -p 3000:3000 --env-file .docker.envrc -t <image name>
```