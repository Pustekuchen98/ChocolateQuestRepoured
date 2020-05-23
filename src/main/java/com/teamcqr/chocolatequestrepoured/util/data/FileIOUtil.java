package com.teamcqr.chocolatequestrepoured.util.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

/**
 * Simple static util class for saving/reading files to/from disk
 *
 * @author jdawg3636
 *         GitHub: https://github.com/jdawg3636
 *
 * @version 05.09.19
 */
public class FileIOUtil {

	// Prevents Instantiation
	private FileIOUtil() {
	}

	// Helper method for saving to world file
	public static String getAbsoluteWorldPath() {
		return DimensionManager.getCurrentSaveRootDirectory() + "/";
	}

	public static void saveToFile(String fileNameIncludingFullPathAndExtension, byte[] toSave) {
		try {
			Files.createDirectories(Paths.get(fileNameIncludingFullPathAndExtension).getParent());
			Files.write(Paths.get(fileNameIncludingFullPathAndExtension), toSave);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] loadFromFile(String fileNameIncludingFullPathAndExtension) {
		try {
			return Files.readAllBytes(Paths.get(fileNameIncludingFullPathAndExtension));
		} catch (Exception e) {
			return null;
		}
	}

	public static void saveNBTCompoundToFile(CompoundNBT root, File file) {
		try {
			OutputStream outStream = null;
			outStream = new FileOutputStream(file);
			CompressedStreamTools.writeCompressed(root, outStream);
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static CompoundNBT getRootNBTTagOfFile(File file) {
		if (file.exists() && file.isFile() && file.getName().contains(".nbt")) {
			InputStream stream = null;
			try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (stream != null) {
				CompoundNBT root = null;
				try {
					root = CompressedStreamTools.readCompressed(stream);
				} catch (IOException ex) {
					// ex.printStackTrace();
					System.out.println("It seems the cqr data file is empty. This is not a problem :). Returning empty tag...");
					root = new CompoundNBT();
				}
				if (root != null) {
					return root;
				}
			}
		}
		return null;
	}

	public static File getOrCreateFile(String FolderPath, String fileName) {
		File folder = new File(FolderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		} else if (!folder.isDirectory()) {
			folder.delete();
			folder.mkdirs();
		}

		File file = new File(folder, fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
				saveNBTCompoundToFile(new CompoundNBT(), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (!file.isFile()) {
			file.delete();
			try {
				file.createNewFile();
				saveNBTCompoundToFile(new CompoundNBT(), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static ListNBT getOrCreateTagList(CompoundNBT rootTag, String key, int listType) {
		ListNBT structureList = new ListNBT();
		if (!rootTag.contains(key, Constants.NBT.TAG_LIST)) {
			if (rootTag.contains(key)) {
				rootTag.remove(key);
			}
			rootTag.put(key, structureList);
		} else {
			structureList = rootTag.getList(key, listType);
		}
		return structureList;
	}

	private static FilenameFilter nbtFileFilter = null;

	public static FilenameFilter getNBTFileFilter() {
		if (nbtFileFilter == null) {
			nbtFileFilter = new FilenameFilter() {

				String[] fileExtensions = new String[] { "nbt" };

				@Override
				public boolean accept(File dirIn, String fileName) {
					if (dirIn != null) {
						if (!dirIn.isDirectory()) {
							return false;
						}

						int var3 = fileName.lastIndexOf(46);
						if (var3 > 0 && var3 < fileName.length() - 1) {
							String var4 = fileName.substring(var3 + 1).toLowerCase();
							String[] var5 = this.fileExtensions;
							int var6 = var5.length;

							for (int var7 = 0; var7 < var6; ++var7) {
								String var8 = var5[var7];
								if (var4.equals(var8)) {
									return true;
								}
							}
						}
					}

					return false;
				}
			};
		}

		return nbtFileFilter;
	}
	
}
