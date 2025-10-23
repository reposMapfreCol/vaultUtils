package co.com.mapfre.vaultUtils.config;

import java.io.Serializable;

public class VaultConfig implements Serializable{
	
	private static final long serialVersionUID = 5464217098498701532L;
	
	private final String vaultUrl;
	private final String roleId;
	private final String secretId;
	private final String secretPath;
	public VaultConfig(String vaultUrl, String roleId, String secretId, String secretPath) {

		this.vaultUrl = vaultUrl;
		this.roleId = roleId;
		this.secretId = secretId;
		this.secretPath = secretPath;
	}
	
	public String getVaultUrl() {
		return vaultUrl;
	}
	public String getRoleId() {
		return roleId;
	}
	public String getSecretId() {
		return secretId;
	}
	public String getSecretPath() {
		return secretPath;
	}	
}
