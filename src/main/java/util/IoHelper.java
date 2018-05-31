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

	public static Context readFirstContext(String dir) {
		for (String zip : findAllZips(dir)) {
			List<Context> ctxs = read(zip);
			return ctxs.get(0);
		}
		return null;
	}

	public static List<Context> readAll(String dir) {
		LinkedList<Context> res = Lists.newLinkedList();

		for (String zip : findAllZips(dir)) {
			res.addAll(read(zip));
		}
		return res;
	}

	public static void createDirectoryIfNotExists(String path) {
		File directory = new File(path);
		if (! directory.exists()){
			directory.mkdir();
		}
	}

	/**
	 * Given a filename and list of API sentences, this method creates a new file with the given filename
	 * and writes all API sentences to that file such that one line contains one sentence.
	 * Sentences are only written to the file if they contain at least as many tokens as defined by minLength.
	 *
	 * @see #removeFile(String)
	 * 			the file with filename is created also if no API sentences are added (due to minLength).
	 * 			Hence, those files must be removed afterwards.
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
	public static void writeAPISentencesToFile(String filename, List<List<APIToken>> apiSentences, int minLength)
			throws IOException {
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		Long sentencesAdded = 0L;
		for(List<APIToken> sentence : apiSentences) {
			if(sentence.size() >= minLength) {
				writer.println(sentence.toString());
				sentencesAdded++;
			}
		}
		writer.close();
		// remove the file created if nothing was written to it
		if(sentencesAdded.equals(0L)) {
			removeFile(filename);
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

	public static void appendClassificationToFile(String filename, APIToken expected, APIToken actual) throws IOException {
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

	public static String pathToFileName(String filePath) {
		return filePath.replaceAll("//","+")
				.replaceAll("/", "+")
				.replaceAll("\\.", "+");
	}

	public static List<Context> read(String zipFile) {
		LinkedList<Context> res = Lists.newLinkedList();
		try {
			IReadingArchive ra = new ReadingArchive(new File(zipFile));
			while (ra.hasNext()) {
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
	public static Set<String> findAllZips(String dir) {
		return new Directory(dir).findFiles(s -> s.endsWith(".zip"));
	}
}