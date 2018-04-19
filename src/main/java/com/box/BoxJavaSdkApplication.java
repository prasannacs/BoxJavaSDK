package com.box;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.box.sdk.BoxConfig;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.IAccessTokenCache;
import com.box.sdk.InMemoryLRUAccessTokenCache;
import com.box.sdk.JWTEncryptionPreferences;
import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.BoxUser;
import com.box.sdk.EncryptionAlgorithm;

@SpringBootApplication
public class BoxJavaSdkApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BoxJavaSdkApplication.class, args);

		/* Connect Box via Dev token
		BoxAPIConnection api = new BoxAPIConnection("t3sT1RK2SAwvqP6NODxhGeabuaVpfHJP");
		BoxFolder rootFolder = BoxFolder.getRootFolder(api);
		for (BoxItem.Info itemInfo : rootFolder) {
		    System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
		}
		*/
		BoxJavaSdkApplication boxsdk = new BoxJavaSdkApplication();
		boxsdk.configureJCE();
		boxsdk.serviceAccountJWTCredentials();
	}
	
	private void configureJCE()	throws Exception {
		// hack for JCE Unlimited Strength
		Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, false);		
	}
	
	private void loadJWTCredentials() throws Exception	{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("box_jwt.json").getFile());
		Reader reader = new FileReader(file);
		BoxConfig boxConfig = BoxConfig.readFrom(reader);
		// Set cache info
		int MAX_CACHE_ENTRIES = 100;
		IAccessTokenCache accessTokenCache = new 
		  InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);

		// Create new app enterprise connection object
		BoxDeveloperEditionAPIConnection client = 
		  BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(boxConfig, accessTokenCache);
		
		BoxUser.Info userInfo = BoxUser.getCurrentUser(client).getInfo();	
		System.out.println("userInfo -- "+userInfo.getName());

	}
	
	private void appuserJWTCrednetials()	{
		JWTEncryptionPreferences jwtPreferences = new JWTEncryptionPreferences();
		jwtPreferences.setPublicKeyID("ypelb739");
		jwtPreferences.setPrivateKeyPassword("c06778815700b335207918a08d1a7576");
		jwtPreferences.setPrivateKey("-----BEGIN ENCRYPTED PRIVATE KEY-----\nMIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQI2HTnWtT4J0wCAggA\nMBQGCCqGSIb3DQMHBAiivC7Qj5s9OQSCBMjWh3RpRHKikeMnFaBWAh6DgmhPrwIo\n4fqc4S+xTOncadn0lXRNw5EK7PDfMBTb4PHBdMz1vS24Or0e+mAULwrW6t17l1K5\nUA3aIlJ8ePxrLyC2v8ocL4rWxfqxKmgZ1oFhHcDKTgm5/duyRFRtK/KBV/NJ/P66\n8mksJJT1kAvqPhYjHAQkBzPjpTA1Mm38bWaqy6/v0e3YIrSxeQXqYhLCEVldEvkx\ntCjCM16HfVqjwRtQ/2OsRedD8muO04lf7QItxEZBtshwwApdvAc5kB0lW+ncNOs6\nJgGuCPzUemR6W4p4bdTI3lbcxwZ8qAVMTEhwejE+tZZin80Z9TjUqhmtVUgLvlwO\nBld35fL0WID9DTP5K1QInxdf2VGFiMBsp9T3fiI4PQCCnXqrOZgA80UAhl4TR87v\nMQmiEdFEdViD766UkUxk2Eo8eErhmDJ/IP2jrDZpKHDylX4ASOMZ1Uape4/mRylm\nW1NvEN9snk92/q0pFcdrpugSYSEWbitfxPDLxHncLcN0EOivMNUOD55sp/160Oct\n/mQWlMTVrcqqPwObuhGEHuLMKH3SX/jfhDanBIc7Ur+N+YYQnKo4y3V0NnaIpRpy\nGBPM12mSHVqa3ekLVMANE2vXhmzHFw4yve18tsv+PiEX1pG0vmuLYkFw8EBGOzFp\nUtBoEoBCUNFANo8O+XyOgG9SfUr/dS+vjguEGkwGufgLzxAe6377kfnkVK5nBOZ8\nDxJM3JpsfQLtZb3tB/eTtax0I4Opnlt563ZBL3WHGUueIrldtvBa/CC2EETfvDpG\nIXDUL0YKs4sLdnxkBWoLpKMdWKMUM4IfIKUrF2O5PABArOXxRSfBLvtDHSTke1V9\nWP08Ga7NyFdg+WmPLwe0MYWsCCueTxlsoL1L6HDjow/nItCEdN9gpVY+as7Z0TU0\nLFxexmV21LIcqxKTThz6LUxbSe5cLyQNhMsPxFwhbEFt4b8HOif8c89Dg/87GyzJ\nVw0N89ODoAvVWpBPL1JlhK0VzwhKEMdZ5hkXb8h4fr8nAEi0mFUXwEyhiY0U57KN\nS4Xu8Tulb9yKjnT8j1AA9ToBLL6fo8SkQxFGiALIPzOZgHChxjRrdCKFBsZxOzwt\nyokOYay99Jr/MPI1lrnUy+7HbCETegEW/fwSfqZh82QL+yfyAdFdt3rz+kn8rFU7\nzfyKVZ2sWEOLNBOU3hyeg49g0BAzfoP721XijfZ6/+mU6+RXTd1IkpLSL8zjSOCu\neoHBdvFzxZtaSKcLEvhbbdGwgnBlj2pwXeDltN7BrrZ9tjMSXg6QX//9rs1Vy4o6\nSnYa5LuJJp1LLWgaKdIhFDI6joXKBlmU4jayQFyJM+F+Nd0T5MadQLhr0emGcKsc\nXweCS8Uy+iv4h4kWZ6VKpnC/QvAlh9+NyaCX1nWDKAaB1A0hiV4NjUi8pdaDgfvf\ncsbdImQg4bE6AfkP5XT7IK/XCYit5YW+eP2n0UQDkPWzLLHK3KeyUD4faAiQOV0L\npyxiY6fKYwJaJGBe0WxpkhwLbGdzbsrdGcZU7mKg5otuOVpBHDgr9ziukcL8tupe\nEurgK5XHzsgNaALEF5p8Htl3RzKFdFdvQoKIGWhIQLEokXS6uIl9Hk3u1UgZX0Rv\nPO0=\n-----END ENCRYPTED PRIVATE KEY-----\n");
		jwtPreferences.setEncryptionAlgorithm(EncryptionAlgorithm.RSA_SHA_256);

		BoxDeveloperEditionAPIConnection api = BoxDeveloperEditionAPIConnection.getAppUserConnection("prasannacs.dev@gmail.com", "74g9ot9m72xaldl6kkcrg5g3k3kxn47c",
		"kE0geL8b1V26k0xAcwupYTQeSzPLWrGW", jwtPreferences);

		BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();	
		System.out.println("userInfo -- "+userInfo.getName());
	}
	
	private void serviceAccountJWTCredentials()	{
		JWTEncryptionPreferences jwtPreferences = new JWTEncryptionPreferences();
		jwtPreferences.setPublicKeyID("ypelb739");
		jwtPreferences.setPrivateKeyPassword("c06778815700b335207918a08d1a7576");
		jwtPreferences.setPrivateKey("-----BEGIN ENCRYPTED PRIVATE KEY-----\nMIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQI2HTnWtT4J0wCAggA\nMBQGCCqGSIb3DQMHBAiivC7Qj5s9OQSCBMjWh3RpRHKikeMnFaBWAh6DgmhPrwIo\n4fqc4S+xTOncadn0lXRNw5EK7PDfMBTb4PHBdMz1vS24Or0e+mAULwrW6t17l1K5\nUA3aIlJ8ePxrLyC2v8ocL4rWxfqxKmgZ1oFhHcDKTgm5/duyRFRtK/KBV/NJ/P66\n8mksJJT1kAvqPhYjHAQkBzPjpTA1Mm38bWaqy6/v0e3YIrSxeQXqYhLCEVldEvkx\ntCjCM16HfVqjwRtQ/2OsRedD8muO04lf7QItxEZBtshwwApdvAc5kB0lW+ncNOs6\nJgGuCPzUemR6W4p4bdTI3lbcxwZ8qAVMTEhwejE+tZZin80Z9TjUqhmtVUgLvlwO\nBld35fL0WID9DTP5K1QInxdf2VGFiMBsp9T3fiI4PQCCnXqrOZgA80UAhl4TR87v\nMQmiEdFEdViD766UkUxk2Eo8eErhmDJ/IP2jrDZpKHDylX4ASOMZ1Uape4/mRylm\nW1NvEN9snk92/q0pFcdrpugSYSEWbitfxPDLxHncLcN0EOivMNUOD55sp/160Oct\n/mQWlMTVrcqqPwObuhGEHuLMKH3SX/jfhDanBIc7Ur+N+YYQnKo4y3V0NnaIpRpy\nGBPM12mSHVqa3ekLVMANE2vXhmzHFw4yve18tsv+PiEX1pG0vmuLYkFw8EBGOzFp\nUtBoEoBCUNFANo8O+XyOgG9SfUr/dS+vjguEGkwGufgLzxAe6377kfnkVK5nBOZ8\nDxJM3JpsfQLtZb3tB/eTtax0I4Opnlt563ZBL3WHGUueIrldtvBa/CC2EETfvDpG\nIXDUL0YKs4sLdnxkBWoLpKMdWKMUM4IfIKUrF2O5PABArOXxRSfBLvtDHSTke1V9\nWP08Ga7NyFdg+WmPLwe0MYWsCCueTxlsoL1L6HDjow/nItCEdN9gpVY+as7Z0TU0\nLFxexmV21LIcqxKTThz6LUxbSe5cLyQNhMsPxFwhbEFt4b8HOif8c89Dg/87GyzJ\nVw0N89ODoAvVWpBPL1JlhK0VzwhKEMdZ5hkXb8h4fr8nAEi0mFUXwEyhiY0U57KN\nS4Xu8Tulb9yKjnT8j1AA9ToBLL6fo8SkQxFGiALIPzOZgHChxjRrdCKFBsZxOzwt\nyokOYay99Jr/MPI1lrnUy+7HbCETegEW/fwSfqZh82QL+yfyAdFdt3rz+kn8rFU7\nzfyKVZ2sWEOLNBOU3hyeg49g0BAzfoP721XijfZ6/+mU6+RXTd1IkpLSL8zjSOCu\neoHBdvFzxZtaSKcLEvhbbdGwgnBlj2pwXeDltN7BrrZ9tjMSXg6QX//9rs1Vy4o6\nSnYa5LuJJp1LLWgaKdIhFDI6joXKBlmU4jayQFyJM+F+Nd0T5MadQLhr0emGcKsc\nXweCS8Uy+iv4h4kWZ6VKpnC/QvAlh9+NyaCX1nWDKAaB1A0hiV4NjUi8pdaDgfvf\ncsbdImQg4bE6AfkP5XT7IK/XCYit5YW+eP2n0UQDkPWzLLHK3KeyUD4faAiQOV0L\npyxiY6fKYwJaJGBe0WxpkhwLbGdzbsrdGcZU7mKg5otuOVpBHDgr9ziukcL8tupe\nEurgK5XHzsgNaALEF5p8Htl3RzKFdFdvQoKIGWhIQLEokXS6uIl9Hk3u1UgZX0Rv\nPO0=\n-----END ENCRYPTED PRIVATE KEY-----\n");
		jwtPreferences.setEncryptionAlgorithm(EncryptionAlgorithm.RSA_SHA_256);

		BoxConfig boxConfig = new BoxConfig("74g9ot9m72xaldl6kkcrg5g3k3kxn47c", "kE0geL8b1V26k0xAcwupYTQeSzPLWrGW", "898202", jwtPreferences);

		BoxDeveloperEditionAPIConnection api = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(boxConfig);
		
		BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();	
		System.out.println("userInfo -- "+userInfo.getName());
		
	}
	
	
}
