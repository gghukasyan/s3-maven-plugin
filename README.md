s3-maven-pluign
===============

Maven plugin to copy assets to S3 bucket.

Usage Example

<plugin>
	<groupId>com.garegin</groupId>
	<artifactId>s3-maven-plugin</artifactId>
	<version>1.0</version>
	<configuration>
		<key>ACCESS_KEY</key>
		<secret>ACCESS_SECRET</secret>
		<bucket>BUCKET</bucket>
		<inputDirectory>INPUT DIRECTORY</inputDirectory>
		<permissions>PublicRead</permissions>
		<includes>
			<include>**/*.*</include>
		</includes>
		<excludes>
			<exclude></exclude>
		</excludes>
	</configuration>
	<executions>
		<execution>
			<goals>
				<goal>upload</goal>
			</goals>
		</execution>
	</executions>
</plugin>
