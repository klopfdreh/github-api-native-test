# GitHub Api Native Test

This is a test readme to be read in the native image

1. Install Docker Desktop
2. (Build) Run `mvn verify` of the root project
3. (GitHub Api Native Test Image) run command `docker build -t testimg .`
4. (GitHub Api Native Test Image) run command `docker images` to find the image id
5. (GitHub Api Native Test Image) run command `docker run --rm -e GITHUB_USERNAME=<username> -e GITHUB_TOKEN=<token> <image_id>` to run the actual image or just `docker run --rm <image_id>` if the repository is public
