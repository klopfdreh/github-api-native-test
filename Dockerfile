FROM redhat/ubi9-minimal:9.3-1552@sha256:582e18f13291d7c686ec4e6e92d20b24c62ae0fc72767c46f30a69b1a6198055

ARG RUNTIMEUSER=1001

ENV ARTIFACT_JAR_PATTERN=github-api-native-test-1.0.0-SNAPSHOT-exe.jar
ENV MAIN_CLASS=github.api.nat.test.GitHubApiNativeTestApplication
ENV BINARY_NAME=github-api-native-test

ENV NIK_TAR_GZ=bellsoft-liberica-vm-openjdk22.0.2+11-24.0.2+1-linux-aarch64.tar.gz
ENV NIK_DOWNLOAD_URL=https://github.com/bell-sw/LibericaNIK/releases/download/24.0.2%2B1-22.0.2%2B11/${NIK_TAR_GZ}
ENV NIK_FOLDER=bellsoft-liberica-vm-openjdk22-24.0.2
ENV NIK_CHECKSUM=d3f49ff70061a1ae51f70514b7501481616adf75

USER root

# Install required tools
RUN microdnf --setopt=install_weak_deps=0 --setopt=tsflags=nodocs install -y tar g++ make zlib-devel gzip findutils
RUN microdnf clean all

# Liberica Native Image Kit
RUN mkdir -p /Library/Java/LibericaNativeImageKit/
WORKDIR /Library/Java/LibericaNativeImageKit/
RUN curl -OL ${NIK_DOWNLOAD_URL}
RUN echo "'$(sha1sum ${NIK_TAR_GZ})' checked against '${NIK_CHECKSUM}  ${NIK_TAR_GZ}'"
RUN if [ $(sha1sum ${NIK_TAR_GZ}) != `echo ${NIK_CHECKSUM}  ${NIK_TAR_GZ}` ]; then exit 1; fi
RUN tar -zxvf ./${NIK_TAR_GZ}
RUN rm ./${NIK_TAR_GZ}
ENV NIK_HOME=/Library/Java/LibericaNativeImageKit/${NIK_FOLDER}/
ENV JAVA_HOME=/Library/Java/LibericaNativeImageKit/${NIK_FOLDER}/
ENV PATH="$PATH:/Library/Java/LibericaNativeImageKit/${NIK_FOLDER}/bin/"

# Build native image
RUN mkdir -p /native-image-build/
COPY ./target/${ARTIFACT_JAR_PATTERN} /native-image-build/
WORKDIR /native-image-build/
COPY InitializeAtBuildTime /native-image-build/
COPY InitializeAtRunTime /native-image-build/
RUN jar -xvf ${ARTIFACT_JAR_PATTERN}
RUN native-image \
--initialize-at-build-time=`cat ./InitializeAtBuildTime | tr '\n' ','` \
--initialize-at-run-time=`cat ./InitializeAtRunTime | tr '\n' ','` \
-H:ReflectionConfigurationResources=META-INF/native-image/github-api-native-test-custom-definitions/reflect-config.json \
--no-fallback \
--enable-https \
--install-exit-handlers \
-H:+UnlockExperimentalVMOptions \
-cp .:BOOT-INF/classes:`find BOOT-INF/lib | tr '\n' ':'` ${MAIN_CLASS} \
-o ${BINARY_NAME}

# Copy native image to destination
RUN mkdir -p /native-image/
RUN mv /native-image-build/${BINARY_NAME} /native-image/${BINARY_NAME}
RUN rm -Rf /native-image-build/

RUN microdnf remove -y tar gcc cpp make g++ zlib-devel gzip findutils

USER ${RUNTIMEUSER}

# For debug
#ENTRYPOINT ["sleep", "infinity"]

# Run binary
ENTRYPOINT [ "bash", "-c", "/native-image/${BINARY_NAME}" ]