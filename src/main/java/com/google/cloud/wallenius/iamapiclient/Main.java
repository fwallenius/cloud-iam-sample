package com.google.cloud.wallenius.iamapiclient;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudresourcemanager.Cloudresourcemanager;
import com.google.api.services.cloudresourcemanager.model.GetIamPolicyRequest;
import com.google.api.services.cloudresourcemanager.model.Policy;
import java.io.IOException;

/**
 *
 * Java implementation of the first sample listed on 
 * <a href="https://cloud.google.com/iam/docs/managing-policies">https://cloud.google.com/iam/docs/managing-policies</a>
 * 
 * The method getResourceManagerClient() can be re-used for trying the other examples.
 * 
 * Transformed to Java 8 syntax for better readability.
 * 
 * @author wallenius
 */
public class Main {

    private static final String PROJECT_ID = "INSERT YOUR PROJECT ID HERE";
    private static Cloudresourcemanager crmClient = null;

    public static void main(String[] args) throws IOException {
        listPoliciesForProject(PROJECT_ID);
    }
    
    /**
     * Get an authenticated client for IAM API. NOTE: It re-uses the bearer created by
     * the gcloud CLI tool. You must first run 'gcloud auth login' for this to work.
     * 
     * @return
     * @throws IOException 
     */
    private static Cloudresourcemanager getResourceManagerClient() throws IOException {

        if (crmClient == null) {
            HttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            GoogleCredential credential = GoogleCredential.getApplicationDefault(transport, jsonFactory);
            Cloudresourcemanager crm = new Cloudresourcemanager.Builder(transport, jsonFactory, null).setApplicationName(PROJECT_ID)
                    .setHttpRequestInitializer(credential).build();
            
            crmClient = crm;
        }
        
        return crmClient;
    }

    public static void listPoliciesForProject(String projectId) throws IOException {

        Cloudresourcemanager crm = getResourceManagerClient();
        Policy policy = crm.projects().getIamPolicy(projectId, new GetIamPolicyRequest()).execute();

        System.out.println(policy);
        
        policy.getBindings().stream()
            .filter((binding) -> (binding.getRole().equals("roles/owner")))
            .forEach((binding) -> {
                binding.getMembers().stream().forEach((member) -> {
                    System.out.println("Found Owners: " + member);
                });
            });
    }

}
