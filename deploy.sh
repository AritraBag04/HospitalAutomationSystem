SERVICES=(
auth
)

echo "Cleaning Maven build..."
mvn clean package -DskipTests|| { echo "Maven clean failed"; exit 1; }

for service in "${SERVICES[@]}"; do
  echo "Building image for $service..."
  mvn compile jib:dockerBuild -pl $service -DskipTests -Dspring.profiles.active=docker || { echo "Failed to build $service"; exit 1; }
done

for service in "${SERVICES[@]}"; do
  echo "Building and pushing image for $service..."

  # Build and push to Docker Hub with versioned tag
  mvn compile jib:build \
    -pl $service \
    -DskipTests \
    -Dspring.profiles.active=docker \
    -Dimage="aritrabag04/${service}-hospital-automation:${VERSION}" \
    -Djib.to.tags="latest" || { echo "Failed to build and push $service"; exit 1; }

  echo "Successfully pushed aritrabag04/${service}-hospital-automation:${VERSION}"
  echo "Also tagged as: aritrabag04/${service}-hospital-automation:latest"
done

echo "All services built and pushed successfully!"
echo "Version: ${VERSION}"
