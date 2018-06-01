/**
 * Copyright 2016 Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * Copyright 2018 Universität Zürich (UZH)
 *
 * Parts of this file were created by members of UZH.
 * The same license applies.
 */
package util;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import extractor.APIToken;

/**
 * this class explains how contexts can be read from the file system
 */
public class IoHelper {

	/**
	 * Create a directory at the given path if it does not yet exist.
	 * 
	 * @param path
	 * 			path to a directory
	 */
	public static void createDirectoryIfNotExists(String path) {
		File directory = new File(path);
		if(!directory.exists()){
			directory.mkdir();
		}
	}

	/**
	 * Given a filename and list of API sentences, this method creates a new file with the given filename
	 * if none exists and appends all API sentences to that file such that one line contains one sentence.
	 * Sentences are only written to the file if they contain at least as many tokens as defined by minLength.
	 *
	 * @param filename
	 * 			file to create and write to
	 * @param apiSentences
	 * 			API sentences that are written to the file
	 * @param minLength
	 * 			minimal length an API sentence must be to be written to the file
	 *
	 * @throws IOException
	 * 			thrown if there is an error with writing or deleting files
	 */
	public static void appendAPISentencesToFile(String filename, List<List<APIToken>> apiSentences, int minLength)
			throws IOException {
		FileWriter fw = new FileWriter(filename, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		for(List<APIToken> sentence : apiSentences) {
			if(sentence.size() >= minLength) {
				for(int i=0;i<sentence.size();i++) {
					out.print(sentence.get(i).getType());
					out.print(",");
					out.print(sentence.get(i).getOperation());
					if(i < sentence.size()-1) {
						out.print(" ");
					}
				}
				out.println();
			}
		}
		out.close();
		bw.close();
		fw.close();
		removeFileIfEmpty(filename);
	}

	/**
	 * Appends the results of a prediction to the given file.
	 * The results of a prediction include tha {@link APIToken} that should be predicted
	 * as well as the {@link APIToken} that was actually predicted.
	 * 
	 * This file can be used in further processing to evaluate the performance of the predictions.
	 * 
	 * @param filename
	 * 			file to store the results
	 * @param expected
	 * 			APIToken that would result in a positive match
	 * @param actual
	 * 			APIToken that was received from the prediction
	 * 			
	 * @throws IOException
	 * 			thrown if the file cannot be read or written to
	 */
	public static void appendPredictionToFile(String filename, APIToken expected, APIToken actual) throws IOException {
		FileWriter fw = new FileWriter(filename, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.print(expected.toString());
		out.print(" ");
		out.print(actual.toString());
		out.println();
		out.close();
		bw.close();
		fw.close();
	}

	/**
	 * Writes the results of an evaluation to a file.
	 * 
	 * @param events
	 * 			number of events processed
	 * @param positives
	 * 			number of positive matches
	 * @param total
	 * 			total number of matches
	 * 		
	 * @throws IOException
	 * 			thrown if the file cannot be created or written
	 */
	public static void writeEvaluationResultsToFile(int events, int positives, int total) throws IOException {
		FileWriter fw = new FileWriter("evalnums.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.println("events: "+events);
		out.println("positives: "+positives);
		out.println("total: "+total);
		out.println("precision: "+((double) positives/ (double) total));
		out.close();
		bw.close();
		fw.close();
	}

	/**
	 * Removes the file at given path if it is empty.
	 * 
	 * @see #removeFile(String)
	 * 			used to remove the file
	 * 
	 * @param filename
	 * 			path to the file that is removed if it is empty
	 * 			
	 * @throws IOException
	 * 			thrown if the file cannot be read or removed
	 */
	public static void removeFileIfEmpty(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		if(br.readLine() == null) {
			removeFile(filename);
		}
	}

	/**
	 * Deletes the file with given path.
	 *
	 * @param filePath
	 * 			path and name of the file
	 *
	 * @throws IOException
	 * 			thrown if there is an error with deleting the file
	 */
	public static void removeFile(String filePath) throws IOException {
		Files.deleteIfExists(new File(filePath).toPath());
	}

	/**
	 * Returns the contexts from the given zip file.
	 * 
	 * @param zipFile
	 * 			zip file containing contexts
	 * @return
	 * 			contexts in the zip file
	 */
	public static List<Context> read(String zipFile) {
		LinkedList<Context> res = Lists.newLinkedList();
		try {
			IReadingArchive ra = new ReadingArchive(new File(zipFile));
			while(ra.hasNext()) {
				res.add(ra.getNext(Context.class));
			}
			ra.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * will recursively search for all .zip files in the "dir". The paths that are
	 * returned are relative to "dir".
	 */

	/**
	 * Returns all .zip files in the given directory.
	 * Notice that the paths that are returned are relative to the given directory.
	 * 
	 * @param dir
	 * 			directory that is traversed
	 * @return
	 * 			all files in the given directory ending in ".zip"
	 */
	public static Set<String> findAllZips(String dir) {
		return new Directory(dir).findFiles(s -> s.endsWith(".zip"));
	}
}