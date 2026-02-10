pipeline {
    agent any

    environment {
        // Docker image tags
        BACKEND_IMAGE = "product-app-backend"
        FRONTEND_IMAGE = "product-app-frontend"
        BUILD_TAG = "${BUILD_NUMBER}"

        // Spring profile for backend
        SPRING_PROFILE = "default"

        // Docker Compose files
        COMPOSE_FILE = "docker-compose.yml"

        // Test results directory
        TEST_RESULTS_DIR = "test-results"
        SCREENSHOTS_DIR = "target/screenshots"
    }

    stages {
        stage('Cleanup Previous Build') {
            steps {
                script {
                    echo "🧹 Cleaning up previous containers and images..."
                    sh '''
                        docker-compose down -v || true
                        docker system prune -f || true
                    '''
                }
            }
        }

        stage('Checkout Code') {
            steps {
                script {
                    echo "📥 Checking out code from repository..."
                    checkout scm
                }
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    echo "🔨 Building Spring Boot backend..."
                    sh '''
                        # Build Maven project
                        mvn clean package -DskipTests

                        # Build Docker image
                        docker build -f Dockerfile.backend -t ${BACKEND_IMAGE}:${BUILD_TAG} .
                        docker tag ${BACKEND_IMAGE}:${BUILD_TAG} ${BACKEND_IMAGE}:latest
                    '''
                }
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    echo "🎨 Building frontend..."
                    sh '''
                        # Build Docker image
                        docker build -f Dockerfile.frontend -t ${FRONTEND_IMAGE}:${BUILD_TAG} .
                        docker tag ${FRONTEND_IMAGE}:${BUILD_TAG} ${FRONTEND_IMAGE}:latest
                    '''
                }
            }
        }

        stage('Start Services') {
            steps {
                script {
                    echo "🚀 Starting all services (backend, frontend, Selenium Grid)..."
                    sh '''
                        # Start all core services
                        docker-compose up -d backend frontend selenium-hub chrome

                        # Wait for services to be healthy
                        echo "⏳ Waiting for services to become healthy..."
                        timeout=180
                        elapsed=0

                        while [ $elapsed -lt $timeout ]; do
                            backend_health=$(docker inspect --format='{{.State.Health.Status}}' backend 2>/dev/null || echo "starting")
                            frontend_health=$(docker inspect --format='{{.State.Health.Status}}' frontend 2>/dev/null || echo "starting")
                            hub_health=$(docker inspect --format='{{.State.Health.Status}}' selenium-hub 2>/dev/null || echo "starting")
                            chrome_health=$(docker inspect --format='{{.State.Health.Status}}' selenium-chrome 2>/dev/null || echo "starting")

                            if [ "$backend_health" = "healthy" ] && \
                               [ "$frontend_health" = "healthy" ] && \
                               [ "$hub_health" = "healthy" ] && \
                               [ "$chrome_health" = "healthy" ]; then
                                echo "✅ All services are healthy!"
                                break
                            fi

                            echo "Status: Backend=$backend_health, Frontend=$frontend_health, Hub=$hub_health, Chrome=$chrome_health"
                            sleep 5
                            elapsed=$((elapsed + 5))
                        done

                        if [ $elapsed -ge $timeout ]; then
                            echo "❌ Services failed to become healthy in time"
                            docker-compose logs
                            exit 1
                        fi

                        # Show running containers
                        docker-compose ps
                    '''
                }
            }
        }

        stage('Run E2E Tests') {
            steps {
                script {
                    echo "🧪 Running E2E tests with Selenium Grid..."
                    sh '''
                        # Create directories for test artifacts
                        mkdir -p ${TEST_RESULTS_DIR}
                        mkdir -p ${SCREENSHOTS_DIR}

                        # Run E2E tests using the profile
                        docker-compose --profile testing run --rm e2e-tests || TEST_EXIT_CODE=$?

                        # Copy test results even if tests failed
                        docker cp e2e-tests:/app/${TEST_RESULTS_DIR}/. ${TEST_RESULTS_DIR}/ 2>/dev/null || true
                        docker cp e2e-tests:/app/${SCREENSHOTS_DIR}/. ${SCREENSHOTS_DIR}/ 2>/dev/null || true

                        # Exit with test result
                        exit ${TEST_EXIT_CODE:-0}
                    '''
                }
            }
            post {
                always {
                    // Archive test results
                    junit allowEmptyResults: true, testResults: '**/test-results/**/*.xml'

                    // Archive screenshots
                    archiveArtifacts artifacts: 'target/screenshots/**/*.png', allowEmptyArchive: true
                }
            }
        }

        stage('Health Check Verification') {
            steps {
                script {
                    echo "🏥 Verifying application health..."
                    sh '''
                        # Check backend health
                        curl -f http://localhost:8080/actuator/health || exit 1

                        # Check frontend accessibility
                        curl -f http://localhost:80/health || curl -f http://localhost:80/ || exit 1

                        # Check Selenium Grid
                        curl -f http://localhost:4444/wd/hub/status || exit 1

                        echo "✅ All health checks passed!"
                    '''
                }
            }
        }

        stage('Tag Images') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "🏷️  Tagging images for deployment..."
                    sh '''
                        # Tag with version number and 'stable'
                        docker tag ${BACKEND_IMAGE}:${BUILD_TAG} ${BACKEND_IMAGE}:stable
                        docker tag ${FRONTEND_IMAGE}:${BUILD_TAG} ${FRONTEND_IMAGE}:stable
                    '''
                }
            }
        }

        stage('Push to Registry') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "📤 Pushing images to Docker registry..."
                    // Uncomment and configure when you have a registry
                    sh '''
                        # Example: Push to Docker Hub
                        # docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}
                        # docker push ${BACKEND_IMAGE}:${BUILD_TAG}
                        # docker push ${FRONTEND_IMAGE}:${BUILD_TAG}
                        # docker push ${BACKEND_IMAGE}:stable
                        # docker push ${FRONTEND_IMAGE}:stable

                        echo "⚠️  Registry push not configured yet"
                        echo "Images tagged locally as ${BUILD_TAG} and stable"
                    '''
                }
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "🚢 Deployment stage..."
                    // Add your deployment logic here
                    sh '''
                        echo "✅ Build ${BUILD_TAG} ready for deployment"
                        echo "Images: ${BACKEND_IMAGE}:${BUILD_TAG}, ${FRONTEND_IMAGE}:${BUILD_TAG}"

                        # Example deployment commands:
                        # - Copy docker-compose.yml to production server
                        # - SSH into server and run: docker-compose pull && docker-compose up -d
                        # - Or use Kubernetes: kubectl apply -f k8s/
                        # - Or use container orchestration platform
                    '''
                }
            }
        }
    }

    post {
        always {
            script {
                echo "🧹 Cleaning up..."
                sh '''
                    # Stop all services
                    docker-compose down -v

                    # Show logs if build failed
                    if [ "${currentBuild.result}" != "SUCCESS" ]; then
                        echo "📋 Dumping logs for debugging..."
                        docker-compose logs --tail=100
                    fi
                '''
            }
        }

        success {
            echo "✅ Build ${BUILD_NUMBER} completed successfully!"
        }

        failure {
            echo "❌ Build ${BUILD_NUMBER} failed!"
            // Add notifications here (email, Slack, etc.)
        }
    }
}